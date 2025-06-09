package com.harmonique.userservice.controller;

import com.harmonique.userservice.payload.UserResponse;
import com.harmonique.userservice.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Controller for admin-level operations like retrieving user data.
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // Ensures all endpoints require ADMIN role
public class AdminController {

    private final UserService userService;

    /**
     * Get user details by email address.
     *
     * @param email the email of the user
     * @return User details in response body
     */
    @Operation(summary = "Get user details by email", description = "Requires ADMIN role")
    @GetMapping("/user/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        log.info("Admin request to get user by email: {}", email);
        UserResponse userResponse = userService.getUserByEmail(email);
        return ResponseEntity.ok(userResponse);
    }

    /**
     * Get details of all registered users.
     *
     * @return List of user details
     */
    @Operation(summary = "Get all registered users", description = "Requires ADMIN role")
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("Admin request to fetch all users");
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}