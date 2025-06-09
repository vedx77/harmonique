package com.harmonique.userservice.controller;

import com.harmonique.userservice.payload.EditUserRequest;
import com.harmonique.userservice.payload.UserResponse;
import com.harmonique.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Get profile of the authenticated user.
     *
     * @param authentication Spring Security authentication object containing user details.
     * @return ResponseEntity containing the user's profile.
     */
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUserProfile(Authentication authentication) {
        String username = authentication.getName();
        log.info("Fetching profile for user: {}", username);

        UserResponse userResponse = userService.getUserProfile(username);
        return ResponseEntity.ok(userResponse);
    }

    /**
     * Edit user profile. Accepts multipart form data including optional profile picture.
     *
     * @param request        EditUserRequest with updated user fields.
     * @param authentication Authenticated user.
     * @return Updated user profile.
     */
    @Operation(summary = "Edit user profile (authenticated)")
    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> editUserProfile(
            @ModelAttribute EditUserRequest request,
            Authentication authentication) {

        String username = authentication.getName();
        log.info("Request to edit profile for user: {}", username);

        try {
            UserResponse response = userService.editUserProfile(username, request);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            log.error("Error while editing profile for user: {}", username, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Upload and update profile picture for authenticated user.
     *
     * @param file           Multipart file to be used as profile picture.
     * @param authentication Authenticated user.
     * @return Updated user profile including profile picture URL.
     */
    @Operation(summary = "Upload profile picture", description = "Upload and update user profile picture")
    @PutMapping(value = "/profile/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> uploadProfilePicture(
            @Parameter(description = "Profile picture file") @RequestPart("file") MultipartFile file,
            Authentication authentication) {

        String username = authentication.getName();
        log.info("Uploading profile picture for user: {}", username);

        try {
            UserResponse updatedResponse = userService.updateProfilePicture(username, file);
            return ResponseEntity.ok(updatedResponse);
        } catch (IOException e) {
            log.error("Error while uploading profile picture for user: {}", username, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}