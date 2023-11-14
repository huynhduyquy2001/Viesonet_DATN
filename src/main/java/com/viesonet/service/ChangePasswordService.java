package com.viesonet.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.viesonet.entity.Accounts;
import com.viesonet.security.AuthConfig;

@Service
public class ChangePasswordService {

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private AuthConfig authConfig;

    public ResponseEntity<?> changePassword(String userId, String matKhau, String matKhauMoi, String matKhauXacNhan) {
        String hashedNewPassword = authConfig.passwordEncoder().encode(matKhauMoi);
        Accounts accounts = accountsService.findByUserId(userId);
        PasswordEncoder passwordEncoder = authConfig.passwordEncoder();

        if (passwordEncoder.matches(matKhau, accounts.getPassword())) {
            if (passwordEncoder.matches(matKhauMoi, accounts.getPassword())) {
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("message", "Mật khẩu mới không được giống mật khẩu cũ"));
            }
            if (!matKhauMoi.equalsIgnoreCase(matKhauXacNhan)) {
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("message", "Mật khẩu và mật khẩu xác nhận không khớp"));
            } else {
                accounts.setPassword(hashedNewPassword);
                accountsService.save(accounts);
                return ResponseEntity.ok().build();
            }
        } else {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Mật khẩu cũ không trùng khớp"));
        }
    }
}
