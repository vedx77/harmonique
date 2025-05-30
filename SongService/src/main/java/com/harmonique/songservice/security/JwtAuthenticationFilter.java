package com.harmonique.songservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Security filter that intercepts each HTTP request and performs JWT validation.
 * If the token is valid, it sets the authentication context for Spring Security.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Extract JWT token from Authorization header
        String header = request.getHeader("Authorization");
        String token = null;

        // Check if header is valid and starts with "Bearer "
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            token = header.substring(7); // Remove "Bearer " prefix
        }

        // Validate token and set authentication context
        if (token != null && jwtTokenHelper.validateToken(token)) {
            String username = jwtTokenHelper.getUsernameFromToken(token);
            List<String> roles = jwtTokenHelper.getRolesFromToken(token);

            // Convert roles (strings) to Spring Security authorities
            var authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // Create authentication token
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            // Set request-specific details and register the auth object
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }
}