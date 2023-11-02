package com.viesonet.security;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Value("${bezkoder.app.jwtSecret}")
    private String jwtSecret;

    @Value("${bezkoder.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${bezkoder.app.jwtRefreshExpirationMs}")
    private int refreshExpirationDateInMs;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        System.out.println("userPrincipal" + userPrincipal.getId());
        return Jwts.builder()
                .setSubject((userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        // Decode chuỗi jwtSecret
        byte[] decodedSecret = Decoders.BASE64.decode(jwtSecret);

        // Encode lại chuỗi jwtSecret
        String encodedSecret = Encoders.BASE64.encode(decodedSecret);
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (JwtException e) {
            logger.error("JWT token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationDateInMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();

    }

    public boolean isJwtTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret) // Thay yourSecretKey bằng khóa bí mật của bạn
                    .parseClaimsJws(token)
                    .getBody();

            // Lấy thời gian hết hạn từ JWT và so sánh với thời gian hiện tại
            long expiration = claims.getExpiration().getTime();
            return expiration < System.currentTimeMillis();
        } catch (Exception e) {
            // Xảy ra lỗi khi phân tích JWT, có thể là JWT không hợp lệ hoặc đã hết hạn
            return true;
        }
    }

}
