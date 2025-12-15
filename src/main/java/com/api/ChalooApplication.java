package com.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ChalooApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChalooApplication.class, args);
	}

}
