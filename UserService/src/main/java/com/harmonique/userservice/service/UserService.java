package com.harmonique.userservice.service;

import java.util.List;

import com.harmonique.userservice.payload.JwtAuthRequest;
import com.harmonique.userservice.payload.JwtAuthResponse;
import com.harmonique.userservice.payload.UserRequest;
import com.harmonique.userservice.payload.UserResponse;

public interface UserService {

    // Fetch user by email
    UserResponse getUserByEmail(String email);
    
    // 1. Register New User
    UserResponse registerUser(UserRequest userRequest);

    // 2. Login (JWT Auth) - Returns token inside response
    JwtAuthResponse login(JwtAuthRequest request);

    // 3. Get Own Profile (by JWT Token User)
    UserResponse getUserProfile(String username);

    // 4. Role Management - Add Role
    String addRole(String roleName);

    // 5. Register New Admin
    UserResponse registerAdmin(UserRequest request);
    
    // 6. Get all User Details
    List<UserResponse> getAllUsers();

}