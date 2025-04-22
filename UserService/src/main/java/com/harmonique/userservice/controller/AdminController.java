package com.harmonique.userservice.controller;

import com.harmonique.userservice.payload.UserResponse;
import com.harmonique.userservice.payload.ApiResponse;
import com.harmonique.userservice.payload.RoleResponse;
import com.harmonique.userservice.service.UserService;
import com.harmonique.userservice.service.RoleService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // Ensures all endpoints require ADMIN role
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    // 1. Get user by email
    @GetMapping("/user/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    // 2. Get all users
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // 3. Get all roles
    @GetMapping("/roles")
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    // 4. Add New Role
    @PostMapping("/addrole")
    public ResponseEntity<ApiResponse> addRole(@RequestParam String roleName) {
        ApiResponse response = ApiResponse.builder()
                .message("Role added successfully")
                .success(true)
                .status(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
