package com.harmonique.songservice.config;

import com.harmonique.songservice.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration for the Song Service.
 * Sets up endpoint access rules and adds JWT authentication filter.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Defines the security filter chain.
     *
     * - Disables CSRF (suitable for stateless APIs)
     * - Allows unrestricted access to Swagger and public GET endpoints
     * - Restricts certain POST routes to ADMIN role only
     * - Adds JWT authentication filter before default login filter
     *
     * @param http the HttpSecurity object
     * @return configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless REST API
            .authorizeHttpRequests(auth -> auth
                // Allow public access to Swagger UI and API docs
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()

                // Allow only ADMINs to add, update, or delete songs
                .requestMatchers(HttpMethod.POST,
                    "api/songs/add",
                    "api/songs/update/**",
                    "api/songs/delete/**"
                ).hasRole("ADMIN")

                // All other endpoints are publicly accessible
                .anyRequest().permitAll()
            )
            // Add JWT filter before Spring Security’s UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}