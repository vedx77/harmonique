package com.harmonique.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class to bootstrap the API Gateway.
 * The gateway serves as the entry point for all client requests,
 * routing them to appropriate microservices.
 */
@SpringBootApplication
public class ApiGatewayApplication {

    private static final Logger logger = LoggerFactory.getLogger(ApiGatewayApplication.class);

    /**
     * Starts the API Gateway application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
        logger.info("API Gateway started successfully.");
    }
}