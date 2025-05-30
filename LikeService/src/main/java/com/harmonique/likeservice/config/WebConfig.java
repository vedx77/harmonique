package com.harmonique.likeservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration class for enabling Cross-Origin Resource Sharing (CORS)
 * to allow requests from the Angular frontend (localhost:4200).
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow all API paths
                .allowedOrigins("http://localhost:4200") // Angular development server
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow common HTTP methods
                .allowCredentials(true); // Allow cookies and credentials
    }
}