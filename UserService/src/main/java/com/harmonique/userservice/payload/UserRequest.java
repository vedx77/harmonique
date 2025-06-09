package com.harmonique.userservice.payload;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * UserRequest is used to accept input from client during user registration/update.
 * Contains validation annotations to ensure input data is clean.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    private String profilePictureUrl;     // Optional: direct URL or null if file is used

    @NotBlank(message = "First name is required !!")
    private String firstName;

    private String lastName;

    @NotBlank(message = "Username is required !!")
    private String username;

    @Email(message = "Invalid email format !!")
    @NotBlank(message = "Email is required !!")
    private String email;

    @NotBlank(message = "Password is required !!")
    @Size(min = 6, message = "Password must be at least 6 characters !!")
    private String password;

    private String phoneNo;

    private String location;

    private String about;

    private MultipartFile file; // Optional: File upload (profile picture)
}