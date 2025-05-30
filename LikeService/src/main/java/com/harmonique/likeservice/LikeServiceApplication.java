package com.harmonique.likeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entry point for the Like-Service microservice in the Harmonique project.
 * Bootstraps the Spring Boot application.
 */
@SpringBootApplication
public class LikeServiceApplication {

	// Logger instance for application startup monitoring
	private static final Logger logger = LoggerFactory.getLogger(LikeServiceApplication.class);

	public static void main(String[] args) {
		logger.info("Starting Like-Service Microservice...");
		SpringApplication.run(LikeServiceApplication.class, args);
		logger.info("Like-Service started successfully!");
	}
}