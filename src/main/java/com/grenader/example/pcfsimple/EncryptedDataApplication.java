package com.grenader.example.pcfsimple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;

@SpringBootApplication
public class EncryptedDataApplication {

	public static void main(String[] args) {
		// Enable encryption for PCF
		Security.setProperty("crypto.policy", "unlimited");

		SpringApplication.run(EncryptedDataApplication.class, args);
	}

}
