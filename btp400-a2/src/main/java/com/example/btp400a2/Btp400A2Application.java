package com.example.btp400a2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class Btp400A2Application {

	public static void main(String[] args) {
		SpringApplication.run(Btp400A2Application.class, args);
	}
	
	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}
}