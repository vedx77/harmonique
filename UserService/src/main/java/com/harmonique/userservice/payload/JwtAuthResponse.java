package com.harmonique.userservice.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO used to send back JWT token and user data upon successful login.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthResponse {

    private String token;          // JWT token used for authentication

    private UserResponse user;    // User info for frontend after login
}