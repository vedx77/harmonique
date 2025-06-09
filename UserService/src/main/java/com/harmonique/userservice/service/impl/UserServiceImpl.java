package com.harmonique.userservice.service.impl;

import com.harmonique.userservice.entity.Role;
import com.harmonique.userservice.entity.User;
import com.harmonique.userservice.exception.EmailAlreadyExistsException;
import com.harmonique.userservice.exception.ResourceNotFoundException;
import com.harmonique.userservice.exception.UsernameAlreadyExistsException;
import com.harmonique.userservice.payload.EditUserRequest;
import com.harmonique.userservice.payload.JwtAuthRequest;
import com.harmonique.userservice.payload.JwtAuthResponse;
import com.harmonique.userservice.payload.UserRequest;
import com.harmonique.userservice.payload.UserResponse;
import com.harmonique.userservice.repository.RoleRepository;
import com.harmonique.userservice.repository.UserRepository;
import com.harmonique.userservice.security.CustomUserDetails;
import com.harmonique.userservice.security.JwtTokenHelper;
import com.harmonique.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the UserService interface.
 * Provides functionality for user registration, authentication,
 * profile editing, and fetching user information.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtTokenHelper jwtTokenHelper;

    @Value("${user.upload.dir}")
    private String uploadDir;

    @Value("${user.access.url}")
    private String uploadAccessUrl;

    /**
     * Registers a new user with the default ROLE_USER and optional profile picture.
     *
     * @param request the user registration details
     * @return the registered user as a UserResponse
     * @throws IOException if the file upload fails
     */
    @Override
    public UserResponse registerUser(UserRequest request) throws IOException {
        log.info("Registering new user: {}", request.getEmail());
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhoneNo(request.getPhoneNo());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAbout(request.getAbout());
        user.setLocation(request.getLocation());

        MultipartFile file = request.getFile();
        if (file != null && !file.isEmpty()) {
            String uploadDir = new File(this.uploadDir).getAbsolutePath();
            File directory = new File(uploadDir);
            if (!directory.exists()) directory.mkdirs();

            String fileName = user.getEmail() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            user.setProfilePictureUrl(fileName);
        }

        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found!"));
        user.setRoles(Set.of(defaultRole));

        userRepository.save(user);
        log.info("User registered successfully: {}", user.getId());

        return mapToUserResponse(user);
    }

    /**
     * Registers an admin with a fixed role ID.
     */
    @Override
    public UserResponse registerAdmin(UserRequest userRequest) {
        log.info("Registering new admin: {}", userRequest.getEmail());
        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .about(userRequest.getAbout())
                .build();

        Role role = roleRepository.findById(3L)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", 3L));
        user.getRoles().add(role);

        User savedUser = userRepository.save(user);
        log.info("Admin registered: {}", savedUser.getId());

        return mapToUserResponse(savedUser);
    }

    /**
     * Edits an existing user's profile.
     */
    @Override
    public UserResponse editUserProfile(String emailFromJwt, EditUserRequest request) throws IOException {
        User user = userRepository.findByEmail(emailFromJwt)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", emailFromJwt));

        log.info("Editing profile for: {}", emailFromJwt);

        if (request.getFirstName() != null && !request.getFirstName().isBlank()) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null && !request.getLastName().isBlank()) user.setLastName(request.getLastName());
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            if (!request.getUsername().equals(user.getUsername()) &&
                userRepository.existsByUsername(request.getUsername())) {
                throw new UsernameAlreadyExistsException("Username already taken.");
            }
            user.setUsername(request.getUsername());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (!request.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
                throw new EmailAlreadyExistsException("Email already in use.");
            }
            user.setEmail(request.getEmail());
        }
        if (request.getPhoneNo() != null && !request.getPhoneNo().isBlank()) user.setPhoneNo(request.getPhoneNo());
        if (request.getAbout() != null && !request.getAbout().isBlank()) user.setAbout(request.getAbout());
        if (request.getLocation() != null && !request.getLocation().isBlank()) user.setLocation(request.getLocation());

        MultipartFile file = request.getFile();
        if (file != null && !file.isEmpty()) {
            String uploadDir = new File(this.uploadDir).getAbsolutePath();
            File directory = new File(uploadDir);
            if (!directory.exists()) directory.mkdirs();

            String fileName = user.getEmail() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            user.setProfilePictureUrl(fileName);
        }

        log.debug("Final profile data before save: {}", user);
        userRepository.save(user);
        return mapToUserResponse(user);
    }

    /**
     * Fetches a user by email.
     */
    @Override
    public UserResponse getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return mapToUserResponse(user);
    }

    /**
     * Authenticates a user and returns a JWT token.
     */
    @Override
    public JwtAuthResponse login(JwtAuthRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Invalid login credentials for: {}", request.getEmail());
            throw new BadCredentialsException("Invalid email or password !!");
        }

        String token = jwtTokenHelper.generateToken(new CustomUserDetails(user));
        log.info("Login successful: {}", user.getId());

        return JwtAuthResponse.builder().token(token).user(mapToUserResponse(user)).build();
    }

    /**
     * Retrieves user profile by email.
     */
    @Override
    public UserResponse getUserProfile(String username) {
        log.info("Retrieving profile for: {}", username);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", username));
        return mapToUserResponse(user);
    }

    /**
     * Updates a user's profile picture.
     */
    @Override
    public UserResponse updateProfilePicture(String username, MultipartFile file) throws IOException {
        log.info("Updating profile picture for: {}", username);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", username));

        String uploadDir = new File(this.uploadDir).getAbsolutePath();
        File directory = new File(uploadDir);
        if (!directory.exists()) directory.mkdirs();

        String fileName = username + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        user.setProfilePictureUrl(fileName);
        userRepository.save(user);

        return mapToUserResponse(user);
    }

    /**
     * Retrieves all users in the system.
     */
    @Override
    public List<UserResponse> getAllUsers() {
        log.info("Fetching all users");
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapToUserResponse).toList();
    }

    /**
     * Converts a User entity to UserResponse DTO.
     */
    private UserResponse mapToUserResponse(User user) {
        UserResponse response = modelMapper.map(user, UserResponse.class);
        response.setRoles(user.getRoles());
        return response;
    }
}