package com.example.common_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommonServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommonServiceApplication.class, args);
	}

//	@Bean
//	NewTopic notification(){
//		return new NewTopic("notification",2,(short)1);
//	}
//
//	@Bean
//	NewTopic statics(){
//		return new NewTopic("static",2,(short) 1);
//	}
}
