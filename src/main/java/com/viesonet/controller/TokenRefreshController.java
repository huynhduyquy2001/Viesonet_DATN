package com.viesonet.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viesonet.entity.JwtResponseModel;
import com.viesonet.security.JwtTokenUtil;

import com.viesonet.service.TokenRefreshService;

import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
public class TokenRefreshController {

    @Autowired
    private TokenRefreshService tokenRefreshService;

    @Autowired
    private JwtTokenUtil jwtUtils;

    @GetMapping("/api/refreshToken")
    public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
        // From the HttpRequest get the claims
        DefaultClaims claims =  (DefaultClaims) request.getAttribute("claims");

        Map<String, Object> expectedMap = tokenRefreshService.getMapFromIoJsonwebtokenClaims(claims);
        String token = jwtUtils.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
        return ResponseEntity.ok(new JwtResponseModel(token));
    }

}
