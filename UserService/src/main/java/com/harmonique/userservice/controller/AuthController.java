package com.harmonique.userservice.controller;

import com.harmonique.userservice.payload.JwtAuthRequest;
import com.harmonique.userservice.payload.JwtAuthResponse;
import com.harmonique.userservice.payload.UserRequest;
import com.harmonique.userservice.payload.UserResponse;
import com.harmonique.userservice.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication and registration-related endpoints.
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;

	/**
	 * Registers a new user (optionally with a profile picture).
	 *
	 * @param userRequest the registration request containing user details
	 * @return the registered user details
	 */
	@Operation(summary = "Register user with optional profile picture")
	@PostMapping(value = "/user/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<UserResponse> registerUserWithPicture(@ModelAttribute @Valid UserRequest userRequest) {
		log.info("Registering new user with username: {}", userRequest.getUsername());

		try {
			UserResponse response = userService.registerUser(userRequest);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("User registration failed for username: {}", userRequest.getUsername(), e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Registers a new admin. Requires a secret key for authorization.
	 *
	 * @param request      the admin registration details
	 * @param adminSecret  optional admin secret key for verification
	 * @return the registered admin details
	 */
	@Operation(summary = "Register new admin (requires secret key)")
	@PostMapping("/admin/register")
	public ResponseEntity<UserResponse> registerAdmin(
			@RequestBody @Valid UserRequest request,
			@RequestParam(required = false) String adminSecret) {

		log.info("Admin registration attempt for username: {}", request.getUsername());

		if (adminSecret == null || !adminSecret.equals("H@rmonique-2025")) {
			log.warn("Invalid admin secret used for registration by username: {}", request.getUsername());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		UserResponse response = userService.registerAdmin(request);
		log.info("Admin registered successfully: {}", response.getUsername());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	/**
	 * Authenticates a user or admin and returns a JWT token.
	 *
	 * @param request login credentials
	 * @return JWT authentication response with token
	 */
	@Operation(summary = "Login for user or admin and return JWT token")
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> login(@RequestBody JwtAuthRequest request) {
		log.info("Login attempt for username/email: {}", request.getEmail());
		JwtAuthResponse authResponse = userService.login(request);
		log.info("Login successful, JWT issued for: {}", request.getEmail());
		return ResponseEntity.ok(authResponse);
	}
}