package com.harmonique.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class to bootstrap the Eureka Server application.
 * This service acts as a discovery server for all microservices in the Harmonique ecosystem.
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(EurekaServerApplication.class);

    /**
     * Main method to start the Eureka Server.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
        logger.info("Eureka Server started successfully.");
    }
}