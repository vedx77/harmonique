package com.harmonique.songservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Entry point for the Song Service microservice in the Harmonique project.
 * This class bootstraps the Spring Boot application.
 */
@SpringBootApplication
@EnableJpaAuditing  // Enables automatic auditing of createdAt and updatedAt fields
public class SongServiceApplication {

    /**
     * Main method to launch the Spring Boot application.
     *
     * @param args Command-line arguments (not used)
     */
	public static void main(String[] args) {
		SpringApplication.run(SongServiceApplication.class, args);
	}
}