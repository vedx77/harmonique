package com.harmonique.userservice.controller;

import com.harmonique.userservice.payload.UserResponse;
import com.harmonique.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 1. Get Own Profile (from JWT token)
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUserProfile(Authentication authentication) {
        String username = authentication.getName();
        UserResponse userResponse = userService.getUserProfile(username);
        return ResponseEntity.ok(userResponse);
    }
}