package com.viesonet.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.entity.Accounts;
import com.viesonet.entity.Users;
import com.viesonet.security.AuthConfig;
import com.viesonet.service.AccountsService;
import com.viesonet.service.ForgotPasswordService;
import com.viesonet.service.SessionService;
import com.viesonet.service.UsersService;

import jakarta.mail.internet.MimeMessage;

@RestController
@CrossOrigin("*")
public class ForgotPasswordController {

	@Autowired
	private ForgotPasswordService forgotPasswordService;

	@Autowired
	private AuthConfig authConfig;

	@Autowired
	private JavaMailSender sender;

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

	@PostMapping("/api/forgetPassword/sendCode")
	public ResponseEntity<?> quenmatkhau(@RequestBody Map<String, Object> data) {
		String phone = (String) data.get("phone");
		MimeMessage message = sender.createMimeMessage();

		// Kiểm tra thời gian chờ giữa các lần gửi mã
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastSentTime < COOLDOWN_PERIOD) {
			long remainingTime = (lastSentTime + COOLDOWN_PERIOD - currentTime) / 1000;
			return ResponseEntity.badRequest().body(Collections.singletonMap("message",
					"Vui lòng đợi " + remainingTime + " giây trước khi gửi lại mã."));
		}

		// Cập nhật thời gian gửi mã cuối cùng
		lastSentTime = currentTime;

		return forgotPasswordService.sendCode(phone, message);

	}

	@PostMapping("/api/forgetPassword")
	public ResponseEntity<?> xacNhan(@RequestBody Map<String, Object> data) {
		String code = (String) data.get("matMa");
		String matKhauMoi = (String) data.get("matKhauMoi");
		String matKhauXacNhan = (String) data.get("matKhauXacNhan");
		String hashedNewPassword = authConfig.passwordEncoder().encode(matKhauMoi);
		PasswordEncoder passwordEncoder = authConfig.passwordEncoder();

		return forgotPasswordService.forgetPass(code, matKhauMoi, matKhauXacNhan, hashedNewPassword, passwordEncoder);
	}

}
