package com.viesonet.handler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication) throws IOException, ServletException {
        // Xử lý các tác vụ sau khi đăng nhập thành công
        // Ví dụ: Ghi log, thực hiện các hành động tùy chỉnh khác
        System.out.println("Đăng nhập thành công!");

        // Chuyển hướng đến trang có URL "/"
        getRedirectStrategy().sendRedirect(request, response, "/");
    }
}

