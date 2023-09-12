package com.viesonet.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.viesonet.security.JwtTokenUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Date;

@Component
public class JwtAuthenticationWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${bezkoder.app.jwtSecret}")
    private String jwtSecret;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Lấy mã thông báo JWT từ máy khách (ví dụ: trong tham số truyền vào của
        // session)
        String jwtToken = session.getAttributes().get("jwtToken").toString();

        if (validateJwtToken(jwtToken)) {
            // Xác thực thành công, cho phép kết nối WebSocket
            super.afterConnectionEstablished(session);
        } else {
            // Xử lý xác thực không thành công và đóng kết nối WebSocket
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid or expired token"));
        }
    }

    // Xác thực JWT Token và kiểm tra tính hợp lệ ở đây
    private boolean validateJwtToken(String jwtToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();

            Date expirationDate = claims.getExpiration();
            Date currentDate = new Date();
            if (expirationDate.before(currentDate)) {
                return false; // Token đã hết hạn
            }

            // Kiểm tra xác thực (ví dụ: kiểm tra tên người dùng)
            String username = claims.getSubject();
            if (username.isEmpty()) {
                return false; // Tên người dùng không hợp lệ
            }

            return true; // Token hợp lệ
        } catch (Exception e) {
            return false; // Lỗi xảy ra hoặc Token không hợp lệ
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Xử lý tin nhắn WebSocket ở đây
    }
}
