package com.harmonique.userservice.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO used during user login.
 * Accepts email and password for authentication.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor 
public class JwtAuthRequest {

    private String email;      // User's email (identifier)

    private String password;   // User's password
}