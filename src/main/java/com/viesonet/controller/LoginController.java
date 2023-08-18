package com.viesonet.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.AuthConfig;
import com.viesonet.entity.Accounts;
import com.viesonet.service.AccountsService;
import com.viesonet.service.CookieService;
import com.viesonet.service.SessionService;
import com.viesonet.service.UsersService;

@RestController
public class LoginController {
	
	@Autowired
	AuthConfig authConfig;

	@GetMapping("/login")
	public ModelAndView getLoginPage() {		
		ModelAndView modelAndView = new ModelAndView("Login");
		return modelAndView;
	}
	@RequestMapping("/login-fail")
    public ModelAndView loginFail() {
        ModelAndView modelAndView = new ModelAndView("Login");
        modelAndView.addObject("message", "Thông tin đăng nhập không đúng!");
        return modelAndView;
    }
	   
}
