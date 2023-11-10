package com.viesonet.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.viesonet.security.AuthConfig;
import com.viesonet.service.AccountStatusService;
import com.viesonet.service.AccountsService;
import com.viesonet.service.RegisterService;
import com.viesonet.service.RolesService;
import com.viesonet.service.UsersService;

@RestController
@CrossOrigin("*")
public class RegisterController {

	@Autowired
	RegisterService RegisterService;

	// Khai báo biến thời gian gửi mã cuối cùng
	private long lastSentTime = 0;
	private static final long COOLDOWN_PERIOD = 60 * 1000; // 1 phút

	@PostMapping("/api/register/sendCode")
	public ResponseEntity<?> sendCode(@RequestBody Map<String, Object> data) {
		String email = (String) data.get("email");
		String username = (String) data.get("username");

		long currentTime = System.currentTimeMillis();

		if (currentTime - lastSentTime < COOLDOWN_PERIOD) {
			long remainingTime = (lastSentTime + COOLDOWN_PERIOD - currentTime) / 1000;
			return ResponseEntity.badRequest().body(Collections.singletonMap("message",
					"Vui lòng đợi " + remainingTime + " giây trước khi gửi lại mã."));
		}

		lastSentTime = currentTime;

		return RegisterService.sendCode(email, username);

	}

	@PostMapping("/api/register")
	public ResponseEntity<?> register(@RequestBody Map<String, Object> data) {
		String email = (String) data.get("email");

		String enteredCode = (String) data.get("code");

		// Thực hiện xử lý đăng ký
		String phoneNumber = (String) data.get("phoneNumber");
		String password = (String) data.get("password");
		String confirmPassword = (String) data.get("confirmPassword");
		String username = (String) data.get("username");
		boolean gender = Boolean.parseBoolean(data.get("gender").toString());
		boolean accept = false;

		if (data.containsKey("accept")) {
			accept = Boolean.parseBoolean(data.get("accept").toString());
		}

		if (!accept) {
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap("message", "Bạn chưa chấp nhận điều khoản"));
		} else {
			return RegisterService.register(phoneNumber, password, confirmPassword, username, gender,
					email, enteredCode);
		}

	}

}
