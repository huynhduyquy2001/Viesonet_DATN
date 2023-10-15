package com.viesonet.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.entity.JwtRequestModel;
import com.viesonet.entity.JwtResponseModel;
import com.viesonet.security.AuthConfig;
import com.viesonet.security.JwtTokenUtil;
import com.viesonet.security.UserDetailsImpl;
import com.viesonet.security.JwtResponse;

@RestController
@CrossOrigin("*")
public class LoginController {

	@Autowired
	AuthConfig authConfig;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenUtil jwtUtils;

	@PostMapping("/api/createToken")
	public ResponseEntity<?> authenticateUser(@Validated @RequestBody JwtRequestModel request) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getPhoneNumber(),
							request.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);

			String jwt = jwtUtils.generateJwtToken(authentication);
			if (authentication.isAuthenticated()) {
				System.out.println("Xac thuc thanh cong");
			} else {
				System.out.println("Xac thuc khong thanh cong");
			}
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream()
					.map(item -> item.getAuthority())
					.collect(Collectors.toList());
			return ResponseEntity.ok(new JwtResponse(jwt,
					userDetails.getId(),
					userDetails.getUsername(),
					userDetails.getEmail(),
					roles));
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai ten dang nhap hoac mat khau");
		}

	}

	@PostMapping("/api/refreshToken")
	public ResponseEntity<?> refreshToken(@RequestBody Map<String, Object> payload) {
		String token = (String) payload.get("token"); // Lấy token từ dữ liệu gửi từ phía frontend

		if (jwtUtils.validateJwtToken(token)) {
			// Token hợp lệ
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String newToken = jwtUtils.generateJwtToken(authentication);
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream()
					.map(item -> item.getAuthority())
					.collect(Collectors.toList());
			return ResponseEntity.ok(new JwtResponse(newToken, userDetails.getId(),
					userDetails.getUsername(),
					userDetails.getEmail(),
					roles));
		} else {
			// Token không hợp lệ
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
		}
	}

}
