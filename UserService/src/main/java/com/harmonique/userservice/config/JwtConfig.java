package com.harmonique.userservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * Configuration class to bind JWT-related properties from application.yml or application.properties.
 * 
 * Properties bound with prefix 'jwt':
 * - jwt.secret : The secret key used to sign JWT tokens
 * - jwt.tokenValidity : Duration in milliseconds for which the token remains valid
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /**
     * Secret key used for signing and verifying JWT tokens.
     */
    private String secret;

    /**
     * Token validity duration in milliseconds.
     */
    private long tokenValidity;
}