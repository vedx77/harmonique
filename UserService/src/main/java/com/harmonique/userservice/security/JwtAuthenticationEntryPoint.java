package com.harmonique.userservice.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This component handles unauthorized access attempts.
 * It is triggered whenever an unauthenticated user tries to access a protected resource.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    /**
     * Called when a user tries to access a protected endpoint without authentication.
     *
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @param authException The exception that caused the entry point to be triggered
     * @throws IOException in case of I/O errors
     * @throws ServletException in case of servlet errors
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
                         throws IOException, ServletException {

        // Log the unauthorized access attempt
        logger.warn("Unauthorized access attempt to URL: {}", request.getRequestURI());
        logger.warn("Authentication exception message: {}", authException.getMessage());

        // Send a 401 Unauthorized response
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized Request - Invalid or Missing Token");
    }
}