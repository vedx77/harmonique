package com.harmonique.userservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the User Service Spring Boot application.
 * This service handles user registration, login, profiles, and role management.
 */
@SpringBootApplication
public class UserServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceApplication.class);

	/**
	 * Main method used to launch the User Service application.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
		logger.info("User Service Application started successfully.");
	}
}