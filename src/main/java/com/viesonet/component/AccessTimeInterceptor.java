package com.viesonet.component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.HandlerInterceptor;

import com.viesonet.security.AuthConfig;
import com.viesonet.entity.Accounts;
import com.viesonet.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AccessTimeInterceptor implements HandlerInterceptor {

    @Autowired
    private UsersService usersService;

    @Autowired
    private AuthConfig authConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            
        	Accounts account = authConfig.getLoggedInAccount(SecurityContextHolder.getContext().getAuthentication());
        	String userId = "";
        	if (account != null) {
        	     userId = account.getUserId();
        	 // Cập nhật thời gian đăng nhập
                usersService.updateLoginTime(userId);
        	    // ...
        	} else {
        	    System.out.println("Account is null.");
        	}
            
        }
        return true;
    }
}
