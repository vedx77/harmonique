package com.harmonique.userservice.service.impl;

import com.harmonique.userservice.entity.Role;
import com.harmonique.userservice.entity.User;
import com.harmonique.userservice.exception.ResourceNotFoundException;
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
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtTokenHelper jwtTokenHelper;

    @Override
    public UserResponse registerUser(UserRequest userRequest) {
        
        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .about(userRequest.getAbout())
                .build();

        Role role = roleRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", 1L));

        user.getRoles().add(role);

        User savedUser = userRepository.save(user);

        return mapToUserResponse(savedUser);
    }

    @Override
    public UserResponse registerAdmin(UserRequest userRequest) {
        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .about(userRequest.getAbout())
                .build();

        Role role = roleRepository.findById(3L)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", 3L));

        user.getRoles().add(role);

        User savedUser = userRepository.save(user);

        return mapToUserResponse(savedUser);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        return mapToUserResponse(user);
    }

    @Override
    public JwtAuthResponse login(JwtAuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password !!");
        }

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String token = jwtTokenHelper.generateToken(userDetails);

        return JwtAuthResponse.builder()
                .token(token)
                .user(mapToUserResponse(user))
                .build();
    }

    @Override
    public UserResponse getUserProfile(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", username));

        return mapToUserResponse(user);
    }

    @Override
    public String addRole(String roleName) {
        boolean roleExists = roleRepository.existsByName(roleName);
        if (roleExists) {
            throw new RuntimeException("Role already exists: " + roleName);
        }

        Role role = Role.builder()
                .name(roleName)
                .build();

        roleRepository.save(role);

        return "Role added successfully: " + roleName;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    // ✅ Reusable helper method
    private UserResponse mapToUserResponse(User user) {
        UserResponse response = modelMapper.map(user, UserResponse.class);
        response.setRoles(user.getRoles()); // ensure roles are set
        return response;
    }
}
