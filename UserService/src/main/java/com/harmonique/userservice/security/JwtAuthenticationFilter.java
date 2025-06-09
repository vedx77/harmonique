package com.harmonique.userservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenHelper jwtTokenHelper;
    private final CustomUserDetailsService customUserDetailsService;
    
    /**
     * This method checks if the current request should NOT be filtered by this JWT filter.
     * It excludes swagger docs, health checks, and login/register endpoints from filtering.
     * 
     * @param request current HTTP request
     * @return true if the filter should be skipped for this request, false otherwise
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        boolean skip = path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/actuator/health")
                || path.startsWith("/auth/login")
                || path.startsWith("/api/users/login")
                || path.startsWith("/api/users/register");

        if (skip) {
            logger.debug("Skipping JWT filter for path: {}", path);
        }

        return skip;
    }

    /**
     * This method is the core filter logic that runs once per request.
     * It extracts the JWT token from the Authorization header,
     * validates it, loads user details, and sets the authentication context.
     * 
     * @param request  HTTP servlet request
     * @param response HTTP servlet response
     * @param filterChain filter chain to continue the filter execution
     * @throws ServletException in case of servlet errors
     * @throws IOException in case of I/O errors
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {

        // 1. Extract Authorization header from the request
        String requestToken = request.getHeader("Authorization");

        String username = null;
        String token = null;

        // 2. Check if header is present and starts with "Bearer "
        if (requestToken != null && requestToken.startsWith("Bearer ")) {
            token = requestToken.substring(7); // Extract the actual token string
            try {
                username = jwtTokenHelper.getUsernameFromToken(token);
                logger.debug("JWT Token extracted for user: {}", username);
            } catch (Exception e) {
                logger.error("Invalid JWT Token: {}", e.getMessage());
            }
        } else {
            // Missing or malformed Authorization header
            logger.warn("JWT Token does not begin with Bearer or is missing");
        }

        // 3. If username was extracted and user is not already authenticated, validate token & set auth
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user details from DB or in-memory
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // Validate token against user details
            if (jwtTokenHelper.validateToken(token, userDetails)) {
                logger.debug("JWT Token is valid. Setting authentication context for user: {}", username);

                // Create authentication token with authorities
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                // Set details like IP address, session id etc.
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the Authentication in SecurityContext for downstream security checks
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.warn("JWT Token validation failed for user: {}", username);
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}