package com.harmonique.userservice.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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

    // 4. Register New Admin
    UserResponse registerAdmin(UserRequest request);
    
    // 5. Get all User Details
    List<UserResponse> getAllUsers();

    // 6. Get Profile pic of user
    UserResponse updateProfilePicture(String username, MultipartFile file) throws IOException;
}