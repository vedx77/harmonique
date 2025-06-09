package com.harmonique.userservice.security;

import com.harmonique.userservice.entity.User;
import com.harmonique.userservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * This service is responsible for loading user-specific data for authentication.
 * It implements Spring Security's UserDetailsService interface.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    /**
     * Loads the user by email (used as username).
     * Converts the User entity into Spring Security's UserDetails object.
     *
     * @param usernameOrEmail email used as login identifier
     * @return UserDetails object required by Spring Security
     * @throws UsernameNotFoundException if user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        logger.info("Attempting to load user by email: {}", usernameOrEmail);

        // Fetch user from the database by email
        User user = userRepository.findByEmail(usernameOrEmail)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", usernameOrEmail);
                    return new UsernameNotFoundException("User not found with email: " + usernameOrEmail);
                });

        logger.info("User found: {}", user.getEmail());

        // Convert roles to SimpleGrantedAuthority for Spring Security
        var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        logger.debug("Assigned roles: {}", authorities);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}