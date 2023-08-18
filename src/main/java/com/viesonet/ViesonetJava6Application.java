package com.viesonet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.viesonet.service.AccountsService;
import com.viesonet.service.RolesService;

@SpringBootApplication
public class ViesonetJava6Application {

	public static void main(String[] args) {
		SpringApplication.run(ViesonetJava6Application.class, args);
	}

}
