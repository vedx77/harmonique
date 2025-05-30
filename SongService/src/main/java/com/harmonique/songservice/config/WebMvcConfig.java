package com.harmonique.songservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration class to expose local file system resources.
 * Specifically, maps uploaded song files to an accessible HTTP path.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Registers a resource handler to serve static song files from the local filesystem.
     *
     * For example, a file at `uploads/songs/song.mp3` will be accessible at
     * `http://localhost:8082/songs/files/song.mp3`
     *
     * @param registry ResourceHandlerRegistry to register mappings
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/songs/files/**") // URL pattern for accessing files
                .addResourceLocations("file:uploads/songs/"); // Maps to local filesystem folder
    }
}