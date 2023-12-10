package com.viesonet.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.viesonet.entity.Accounts;
import com.viesonet.entity.Users;

import jakarta.mail.internet.MimeMessage;

@Service
public class ForgotPasswordService {
    @Autowired
    private AccountsService accountsService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private JavaMailSender sender;

    @Autowired
    private SessionService sessionService;

    private int[] randomNumbers;

    String savedEmail;

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

    public ResponseEntity<?> sendCode(String email, MimeMessage message) {
        Accounts accounts = accountsService.findByEmail(email);
        randomNumbers = getRandomNumbers();

        if (accounts == null) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Tài khoản này chưa được đăng ký!"));
        }
        // Nếu expectedCode đã hết thời gian, đặt nó thành null
        if (randomNumbers != null) {
            codeExpirationScheduler.schedule(() -> {
                randomNumbers = null;
            }, MAX_CODE_LIFETIME, TimeUnit.MILLISECONDS);

        }

        if (accounts.getAccountStatus().getStatusId() == 4) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Tài khoản này đã bị khóa !"));
        } else {
            Users users = usersService.getById(accounts.getUser().getUserId());
            savedEmail = email;
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
                helper.setFrom("VIE_SONET");
                helper.setTo(accounts.getEmail());
                helper.setSubject("MẬT MÃ CỦA BẠN - Vie_SoNet");

                helper.setText("XIN CHÀO BẠN " + "<h3>" + users.getUsername() + "</h3>" + "<br>"
                        + "Mình ở đây để gửi mật mã cho bạn và mật mã của bạn là: " + "<h3>"
                        + Arrays.toString(randomNumbers).replaceAll("\\[|\\]|,|\\s", "") + "<h3> <br>"
                        + "Bạn nhớ là đừng để mật mã mình bị lộ ra ngoài nha!" + "<br>" + "Vie_Sonet THÂN CHÀO!", true);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Mật mã chưa được gửi !"));
            }
            sender.send(message);
            return ResponseEntity.ok().build();
        }
    }

    public ResponseEntity<?> forgetPass(String code, String matKhauMoi, String matKhauXacNhan, String hashedNewPassword,
            PasswordEncoder passwordEncoder) {
        Accounts accounts = accountsService.findByEmail(savedEmail);

        String expectedCode = Arrays.toString(randomNumbers).replaceAll("\\[|\\]|,|\\s", "");

        if (passwordEncoder.matches(matKhauMoi, accounts.getPassword())) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Mật khẩu mới không được giống mật khẩu cũ"));
        }
        if (!matKhauMoi.equalsIgnoreCase(matKhauXacNhan)) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Mật khẩu và mật khẩu xác nhận không khớp"));
        }
        if (!code.equals(expectedCode)) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message",
                    "Mã xác nhận chưa chính xác hoặc mã đã hết hạn. Vui lòng thử lại!"));
        } else {
            accounts.setPassword(hashedNewPassword);
            accountsService.save(accounts);
            return ResponseEntity.ok().build();
        }
    }
}
