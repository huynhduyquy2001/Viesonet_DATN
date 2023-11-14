package com.viesonet.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.viesonet.entity.Accounts;
import com.viesonet.entity.Users;
import com.viesonet.security.AuthConfig;

import jakarta.mail.internet.MimeMessage;

@Service
public class RegisterService {
    @Autowired
    AccountsService accountsService;

    @Autowired
    UsersService usersService;

    @Autowired
    RolesService rolesService;

    @Autowired
    AuthConfig authConfig;

    @Autowired
    AccountStatusService accountStatusService;

    @Autowired
    JavaMailSender sender;

    private int[] randomNumbers;

    private static final long MAX_CODE_LIFETIME = 60 * 1000;

    // ScheduledExecutorService để đếm ngược thời gian
    private ScheduledExecutorService codeExpirationScheduler = Executors.newSingleThreadScheduledExecutor();

    public int[] getRandomNumbers() {
        Random random = new Random();
        int[] numbers = new int[4];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = random.nextInt(10);
        }
        return numbers;
    }

    public String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public String generateRandomNumbers() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int randomNumber = random.nextInt(10); // Sinh số ngẫu nhiên từ 0 đến 9
            sb.append(randomNumber);
        }
        return sb.toString();
    }

    String numbers = generateRandomNumbers();
    String randomString = generateRandomString();
    String id = randomString + numbers;

    String savedEmail = ""; // Lưu email đã được gửi mã
    String savedCode = ""; // Lưu mã đã được gửi

    public ResponseEntity<?> sendCode(String email, String username) {
        MimeMessage message = sender.createMimeMessage();

        // Nếu expectedCode đã hết thời gian, đặt nó thành null
        if (randomNumbers != null) {
            codeExpirationScheduler.schedule(() -> {
                randomNumbers = null;
            }, MAX_CODE_LIFETIME, TimeUnit.MILLISECONDS);

        }

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom("VIE_SONET");
            helper.setTo(email);
            helper.setSubject("MẬT MÃ CỦA BẠN - Vie_SoNet");
            randomNumbers = getRandomNumbers();
            helper.setText("XIN CHÀO BẠN " + "<h3>" + username + "</h3>" + "<br>"
                    + "Chào mừng bạn đã đến với thế giới Vie_SoNet ! " + "<br>"
                    + "Đây là mã xác thực người dùng của bạn: " + "<h3>"
                    + Arrays.toString(randomNumbers).replaceAll("\\[|\\]|,|\\s", "") + "</h3> <br>"
                    + "Vui lòng không để mật khẩu lộ ra ngoài !" + "<br>"
                    + "Chúc bạn sẽ có những giây phút trải nghiệm thú vị với Vie_SoNet" + "<br>"
                    + "Vie_Sonet THÂN CHÀO!", true);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Có lỗi xảy ra. Vui lòng thử lại!"));
        }

        sender.send(message);
        savedEmail = email; // Lưu email
        System.out.println("Email đã lưu nè: " + savedEmail);
        savedCode = Arrays.toString(randomNumbers).replaceAll("\\[|\\]|,|\\s", ""); // Lưu mã
        return ResponseEntity.ok().build();

    }

    public ResponseEntity<?> register(String phoneNumber, String password, String confirmPassword, String username,
            boolean gender,
            String email, String enteredCode) {

        if (enteredCode == null) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Bạn chưa nhập mã xác thực"));
        }
        if (!enteredCode.equals(savedCode)) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message",
                            "Mã xác thực không đúng hoặc đã hết hạn. Vui lòng thử lại!"));
        }
        if (accountsService.existByEmail(email)) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Email này đã được đăng ký"));
        }
        if (accountsService.existById(phoneNumber)) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Số điện thoại này đã được đăng ký"));
        }
        if (!email.equalsIgnoreCase(savedEmail)) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Email này chưa xác thực"));
        } else {
            if (password.equalsIgnoreCase(confirmPassword)) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, -18); // Trừ 18 năm từ ngày hiện tại
                Date eighteenYearsAgo = cal.getTime();

                String hashedPassword = authConfig.passwordEncoder().encode(password);
                Users user = new Users();
                user.setAvatar(gender ? "avatar1.jpg" : "avatar2.jpg");
                user.setViolationCount(0);
                user.setBackground("nen.jpg");
                user.setCreateDate(new Date());
                user.setUserId(id);
                user.setBirthday(eighteenYearsAgo);
                user.setUsername(username);
                usersService.save(user);

                Accounts account = new Accounts();
                account.setPhoneNumber(phoneNumber);
                account.setPassword(hashedPassword);
                account.setEmail(email);
                account.setUser(usersService.getById(id));
                account.setRole(rolesService.getById(3));
                account.setAccountStatus(accountStatusService.getById(1));
                accountsService.save(account);

                savedEmail = "";
                savedCode = "";
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("message", "Mật khẩu và mật khẩu xác nhận không khớp"));
            }
        }

    }
}
