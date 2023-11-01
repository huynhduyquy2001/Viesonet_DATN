package com.viesonet.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.security.AuthConfig;
import com.viesonet.entity.Accounts;
import com.viesonet.service.AccountsService;
import com.viesonet.service.SessionService;

@RestController
public class ChangePasswordController {
	@Autowired
	private SessionService sessionService;

	@Autowired
	private AccountsService accountsService;

	@Autowired
	private AuthConfig authConfig;

	@GetMapping("/changepassword")
	public ModelAndView getChangePasswordPage() {
		ModelAndView modelAndView = new ModelAndView("ChangePassword");
		return modelAndView;
	}

	@PostMapping("/doimatkhau")
	public ResponseEntity<?> doimatkhau(@RequestBody Map<String, Object> data, Authentication authentication) {
		Accounts account = authConfig.getLoggedInAccount(authentication);
		String matKhau = (String) data.get("matKhau");
		String matKhauMoi = (String) data.get("matKhauMoi");
		String matKhauXacNhan = (String) data.get("matKhauXacNhan");

		String hashedNewPassword = authConfig.passwordEncoder().encode(matKhauMoi);
		Accounts accounts = accountsService.findByPhoneNumber(account.getPhoneNumber());
		PasswordEncoder passwordEncoder = authConfig.passwordEncoder();

		if (passwordEncoder.matches(matKhau, accounts.getPassword())) {
			if (passwordEncoder.matches(matKhauMoi, accounts.getPassword())) {
				return ResponseEntity.ok()
						.body(Collections.singletonMap("message", "Mật khẩu mới không được giống mật khẩu cũ"));
			}
			if (!matKhauMoi.equalsIgnoreCase(matKhauXacNhan)) {
				return ResponseEntity.ok()
						.body(Collections.singletonMap("message", "Mật khẩu và mật khẩu xác nhận không khớp"));
			} else {
				account.setPassword(hashedNewPassword);
				accountsService.save(accounts);
				return ResponseEntity.ok().build();
			}
		} else {
			return ResponseEntity.ok().body(Collections.singletonMap("message", "Mật khẩu cũ không trùng khớp"));
		}
	}

}
