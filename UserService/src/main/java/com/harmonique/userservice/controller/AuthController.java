package com.harmonique.userservice.controller;

import com.harmonique.userservice.payload.JwtAuthRequest;
import com.harmonique.userservice.payload.JwtAuthResponse;
import com.harmonique.userservice.payload.UserRequest;
import com.harmonique.userservice.payload.UserResponse;
import com.harmonique.userservice.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // 1. Register Normal User
//    @PostMapping("/user/register")
//    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest request) {
//        return new ResponseEntity<>(userService.registerUser(request), HttpStatus.CREATED);
//    }
    
    // 2. Register User With Profile Picture
//    @Operation(summary = "Register user with optional profile picture")
//    @PostMapping(value = "/user/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<UserResponse> registerUserWithPicture(
//            @RequestPart("user") UserRequest userRequest,
//            @RequestPart(value = "file", required = false) MultipartFile file) {
//
//        try {
//            UserResponse response = userService.registerUser(userRequest, file);
//            return new ResponseEntity<>(response, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    
    @Operation(summary = "Register user with optional profile picture")
    @PostMapping(value = "/user/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> registerUserWithPicture(
            @ModelAttribute @Valid UserRequest userRequest) {

        try {
            UserResponse response = userService.registerUser(userRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 2. Register Admin (with optional secret)
    @PostMapping("/admin/register")
    public ResponseEntity<UserResponse> registerAdmin(@RequestBody UserRequest request,
                                                      @RequestParam(required = false) String adminSecret) {

        if (adminSecret == null || !adminSecret.equals("H@rmonique-2025")) {
            throw new RuntimeException("Invalid Admin Secret Key");
        }

        return new ResponseEntity<>(userService.registerAdmin(request), HttpStatus.CREATED);
    }

    // 3. Login (common for both - just depends on credentials)
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody JwtAuthRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
}