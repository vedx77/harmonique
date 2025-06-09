package com.harmonique.userservice.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.harmonique.userservice.payload.EditUserRequest;
import com.harmonique.userservice.payload.JwtAuthRequest;
import com.harmonique.userservice.payload.JwtAuthResponse;
import com.harmonique.userservice.payload.UserRequest;
import com.harmonique.userservice.payload.UserResponse;

/**
 * UserService defines the core operations related to user management
 * such as registration, authentication, profile updates, and retrieval.
 */
public interface UserService {

    /**
     * Fetch a user's details by their email.
     *
     * @param email Email address of the user.
     * @return UserResponse containing user information.
     */
    UserResponse getUserByEmail(String email);

    /**
     * Register a new user with the given details.
     *
     * @param request UserRequest containing registration details.
     * @return UserResponse with saved user information.
     * @throws IOException If file upload fails.
     */
    UserResponse registerUser(UserRequest request) throws IOException;

    /**
     * Authenticate a user and return JWT token.
     *
     * @param request JwtAuthRequest containing login credentials.
     * @return JwtAuthResponse containing JWT token and user info.
     */
    JwtAuthResponse login(JwtAuthRequest request);

    /**
     * Get the profile of the logged-in user.
     *
     * @param username The username (email) extracted from JWT token.
     * @return UserResponse containing user profile.
     */
    UserResponse getUserProfile(String username);

    /**
     * Register a new admin account.
     *
     * @param request UserRequest containing admin registration data.
     * @return UserResponse with admin user info.
     */
    UserResponse registerAdmin(UserRequest request);

    /**
     * Retrieve a list of all users.
     *
     * @return List of UserResponse for all registered users.
     */
    List<UserResponse> getAllUsers();

    /**
     * Update profile picture for the given user.
     *
     * @param username Email or username of the user.
     * @param file Multipart file to upload as profile picture.
     * @return Updated UserResponse with new profile picture URL.
     * @throws IOException If file upload fails.
     */
    UserResponse updateProfilePicture(String username, MultipartFile file) throws IOException;

    /**
     * Edit and update profile details of a user.
     *
     * @param username Email of the user (from JWT).
     * @param request EditUserRequest containing fields to update.
     * @return Updated UserResponse with new details.
     * @throws IOException If file upload fails.
     */
    UserResponse editUserProfile(String username, EditUserRequest request) throws IOException;
}