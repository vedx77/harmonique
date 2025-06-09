package com.harmonique.userservice.config;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

    /**
     * Configures resource handlers to serve static resources.
     * 
     * This method maps any requests starting with /profilepic/** to the physical
     * folder on the server where user profile pictures are stored, allowing these images
     * to be served directly via HTTP.
     *
     * @param registry ResourceHandlerRegistry to register resource handlers.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Resolve the absolute path to the profile pictures directory
        String profilePicPath = "file:" + Paths.get("uploads/profilepic/").toAbsolutePath() + "/";
        
        // Log the resource handler registration for debugging
        logger.info("Registering resource handler for /profilepic/** mapped to {}", profilePicPath);

        // Register resource handler for profile pictures
        registry.addResourceHandler("/profilepic/**")
                .addResourceLocations(profilePicPath);
    }
}