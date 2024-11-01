package com.example.users_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@SpringBootApplication
@Configuration
public class UsersServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersServiceApplication.class, args);
	}

//	@Bean
//	public AuthenticationManager authenticationManager() {
//		return new AuthenticationManager() {
//			@Override
//			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//				return null;
//			}
//		};
//	}

}
