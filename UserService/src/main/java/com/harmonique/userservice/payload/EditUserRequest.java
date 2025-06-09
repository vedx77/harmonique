package com.harmonique.userservice.payload;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

/**
 * DTO used when editing/updating user profile details.
 * Accepts both text fields and optional file upload for profile picture.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditUserRequest {

    private String firstName;          // Optional update for first name

    private String lastName;           // Optional update for last name

    private String username;           // Optional update for username

    private String email;              // Optional update for email

    private String phoneNo;            // Optional update for phone number

    private String location;           // Optional update for location

    private String about;              // Optional update for bio/about

    private MultipartFile file;        // Optional file upload for profile picture
}