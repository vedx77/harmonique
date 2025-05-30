package com.harmonique.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller to handle root ("/") and fallback error ("/error") endpoints
 * when no matching route is found in the API Gateway.
 */
@RestController
public class FallbackController {

    // Logger instance for debugging/fallback monitoring
    private static final Logger log = LoggerFactory.getLogger(FallbackController.class);

    /**
     * Default welcome endpoint for the API Gateway root.
     * Useful to verify if the Gateway is running.
     *
     * @return A welcome message
     */
    @GetMapping("/")
    public String home() {
        log.info("Root path '/' accessed - API Gateway is active.");
        return "Welcome to Harmonique API Gateway!";
    }

    /**
     * Handles unmatched routes or errors triggered in routing.
     *
     * @return Custom error message for unknown routes
     */
    @GetMapping("/error")
    public String error() {
        log.warn("Fallback route '/error' hit - Possibly unmatched route or fallback.");
        return "Custom Error Page - Route not found";
    }
}