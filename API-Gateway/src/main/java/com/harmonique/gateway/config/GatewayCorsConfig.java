package com.harmonique.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up CORS in the API Gateway.
 */
@Configuration
public class GatewayCorsConfig {

    private static final Logger logger = LoggerFactory.getLogger(GatewayCorsConfig.class);

    /**
     * Defines a WebMvcConfigurer bean to configure global CORS mappings.
     * 
     * @return WebMvcConfigurer with customized CORS mappings.
     */
    @Bean
    WebMvcConfigurer corsConfigurer() {
        logger.info("Configuring CORS settings for API Gateway...");

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200") // Frontend origin (Angular dev server)
                        .allowedMethods("*")                     // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
                        .allowedHeaders("*")                     // Allow all headers
                        .allowCredentials(true);                 // Allow cookies/credentials

                logger.info("CORS mappings applied: Allowed origin http://localhost:4200");
            }
        };
    }
}