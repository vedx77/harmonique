package com.harmonique.userservice.payload;

import com.harmonique.userservice.entity.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * UserResponse is a Data Transfer Object used to send user data in responses.
 * It omits sensitive information like password and presents a read-only structure.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;                       // Unique user ID

    private String profilePictureUrl;     // URL to profile picture

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String phoneNo;

    private String about;                 // User bio

    private String location;

    private Set<Role> roles;              // Roles assigned to the user

    private LocalDateTime createdAt;      // Auto-set when user is created
    private LocalDateTime updatedAt;      // Auto-updated on modification
}