package com.example.common_service;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.internals.Topic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@SpringBootApplication
public class CommonServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommonServiceApplication.class, args);
	}

	@Bean
	NewTopic notification(){
		return new NewTopic("notification",2,(short)1);
	}

	@Bean
	NewTopic statics(){
		return new NewTopic("static",2,(short) 1);
	}
}
