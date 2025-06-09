package com.harmonique.userservice.config;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Application-level configuration class.
 * Defines common beans that are used across the User Service application.
 */
@Configuration
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    /**
     * Provides a singleton ModelMapper bean used for mapping DTOs and entities.
     *
     * @return a configured ModelMapper instance
     */
    @Bean
    public ModelMapper modelMapper() {
        logger.info("ModelMapper bean initialized.");
        return new ModelMapper();
    }
}