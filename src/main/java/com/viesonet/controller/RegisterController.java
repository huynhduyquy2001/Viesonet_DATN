package com.viesonet.controller;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.AuthConfig;
import com.viesonet.entity.Accounts;
import com.viesonet.entity.Users;
import com.viesonet.service.AccountStatusService;
import com.viesonet.service.AccountsService;
import com.viesonet.service.RolesService;
import com.viesonet.service.UsersService;

import jakarta.mail.internet.MimeMessage;

@RestController
public class RegisterController {
	@Autowired
	AccountsService accountsService;

	@Autowired
	UsersService usersService;

	@Autowired
	RolesService rolesService;

	@Autowired
	AccountStatusService accountStatusService;

	@Autowired
	AuthConfig authConfig;

	@Autowired
	JavaMailSender sender;

	private int[] randomNumbers;

	// Khai báo biến thời gian gửi mã cuối cùng
	private long lastSentTime = 0;
	private static final long COOLDOWN_PERIOD = 60 * 1000; // 1 phút

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
	boolean hasSentVerificationCode = false; // Biến để kiểm tra xem đã gửi mã hay chưa
	String savedEmail = ""; // Lưu email đã được gửi mã
	String savedCode = ""; // Lưu mã đã được gửi

	@GetMapping("/register")
	public ModelAndView getRegisterPage() {
		ModelAndView modelAndView = new ModelAndView("Register");
		return modelAndView;
	}

	@PostMapping("/dangky/guima")
	public ResponseEntity<?> guiMa(@RequestBody Map<String, Object> data) {
		String email = (String) data.get("email");
		String username = (String) data.get("username");
		MimeMessage message = sender.createMimeMessage();

		// Kiểm tra thời gian chờ giữa các lần gửi mã
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastSentTime < COOLDOWN_PERIOD) {
			long remainingTime = (lastSentTime + COOLDOWN_PERIOD - currentTime) / 1000;
			return ResponseEntity.ok().body(Collections.singletonMap("message",
					"Vui lòng đợi " + remainingTime + " giây trước khi gửi lại mã."));
		}

		// Cập nhật thời gian gửi mã cuối cùng
		lastSentTime = currentTime;

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
			return ResponseEntity.ok().body(Collections.singletonMap("message", "Hãy điền thông tin vào trước nhé !"));
		}
		sender.send(message);
		savedEmail = email; // Lưu email
		savedCode = Arrays.toString(randomNumbers).replaceAll("\\[|\\]|,|\\s", ""); // Lưu mã
		hasSentVerificationCode = true;
		return ResponseEntity.ok().build();

	}

	@PostMapping("/dangky")
	public ResponseEntity<?> dangKy2(@RequestBody Map<String, Object> data) {
		String email = (String) data.get("email");

		if (hasSentVerificationCode && email.equals(savedEmail)) {
			String enteredCode = (String) data.get("code");

			if (!enteredCode.equals(savedCode)) {
				return ResponseEntity.ok().body(Collections.singletonMap("message", "Mật mã chưa chính xác!"));
			}

			// Thực hiện xử lý đăng ký
			String phoneNumber = (String) data.get("phoneNumber");
			String password = (String) data.get("password");
			String confirmPassword = (String) data.get("confirmPassword");
			String username = (String) data.get("username");
			boolean gender = Boolean.parseBoolean(data.get("gender").toString());
			boolean accept = false;

			String expectedCode = Arrays.toString(randomNumbers).replaceAll("\\[|\\]|,|\\s", "");

			if (!enteredCode.equals(expectedCode)) {
				return ResponseEntity.ok().body(Collections.singletonMap("message", "Mật mã chưa chính xác!"));
			}

			if (data.containsKey("accept")) {
				accept = Boolean.parseBoolean(data.get("accept").toString());
			}

			if (!accept) {
				return ResponseEntity.ok()
						.body(Collections.singletonMap("message", "Phải chấp nhận điều khoản để đăng kí tài khoản"));
			} else {
				if (!accountsService.existById(phoneNumber)) {
					if (accountsService.existByEmail(email)) {
						return ResponseEntity.ok()
								.body(Collections.singletonMap("message", "Email này đã được đăng ký"));
					} else {
						if (password.equalsIgnoreCase(confirmPassword)) {

							// Tính toán ngày sinh dựa trên tuổi 18
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

							// Reset trạng thái sau khi đăng ký thành công
							hasSentVerificationCode = false;
							savedEmail = "";
							savedCode = "";

							return ResponseEntity.ok().build();
						} else {
							return ResponseEntity.ok().body(Collections.singletonMap("message",
									"Mật khẩu và mật khẩu xác nhận không trùng khớp"));
						}
					}
				} else {
					return ResponseEntity.ok().body(Collections.singletonMap("message", "Tài khoản đã tồn tại"));
				}
			}
		} else {
			return ResponseEntity.ok()
					.body(Collections.singletonMap("message", "Vui lòng hoàn thành bước xác thực trước khi đăng ký"));
		}
	}

}
