package com.viesonet.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.viesonet.service.ChangePasswordService;

@RestController
@CrossOrigin("*")
public class ChangePasswordController {
	@Autowired
	ChangePasswordService changePasswordService;

	@PostMapping("/api/changePassword")
	public ResponseEntity<?> doimatkhau(@RequestBody Map<String, Object> data) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		String matKhau = (String) data.get("matKhau");
		String matKhauMoi = (String) data.get("matKhauMoi");
		String matKhauXacNhan = (String) data.get("matKhauXacNhan");

		System.out.println("Đây là userId: " + SecurityContextHolder.getContext().getAuthentication().getName());

		return changePasswordService.changePassword(userId, matKhau, matKhauMoi, matKhauXacNhan);
	}

}
