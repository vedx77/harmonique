package com.harmonique.userservice.service.impl;

import com.harmonique.userservice.entity.Role;
import com.harmonique.userservice.entity.User;
import com.harmonique.userservice.exception.EmailAlreadyExistsException;
import com.harmonique.userservice.exception.MissingContactInformationException;
import com.harmonique.userservice.exception.ResourceNotFoundException;
import com.harmonique.userservice.exception.UsernameAlreadyExistsException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

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

//    @Override
//    public UserResponse registerUser(UserRequest userRequest) {
//    	
//    	// 1. At least one contact method must be provided
//        if ((userRequest.getEmail() == null || userRequest.getEmail().isBlank()) &&
//            (userRequest.getPhoneNo() == null || userRequest.getPhoneNo().isBlank())) {
//            throw new MissingContactInformationException("Either email or phone number must be provided");
//        }
//        
//        // 2. Uniqueness checks
//        if (userRepository.existsByEmail(userRequest.getEmail())) {
//            throw new EmailAlreadyExistsException("Email already in use");
//        }
//
//        if (userRepository.existsByUsername(userRequest.getUsername())) {
//            throw new UsernameAlreadyExistsException("Username already in use");
//        }
//
//        User user = User.builder()
//                .firstName(userRequest.getFirstName())
//                .lastName(userRequest.getLastName())
//                .username(userRequest.getUsername())
//                .email(userRequest.getEmail())
//                .password(passwordEncoder.encode(userRequest.getPassword()))
//                .profilePictureUrl(userRequest.getProfilePictureUrl())
//                .phoneNo(userRequest.getPhoneNo())
//                .about(userRequest.getAbout())
//                .location(userRequest.getLocation())
//                .build();
//
//        Role role = roleRepository.findById(1L)
//                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", 1L));
//
//        user.getRoles().add(role);
//
//        User savedUser = userRepository.save(user);
//
//        return mapToUserResponse(savedUser);
//    }
    
//    @Override
//    public UserResponse registerUser(UserRequest request, MultipartFile file) throws IOException {
//        User user = new User();
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setUsername(request.getUsername());
//        user.setEmail(request.getEmail());
//        user.setPhoneNo(request.getPhoneNo());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setAbout(request.getAbout());
//        user.setLocation(request.getLocation());
//
//        if (file != null && !file.isEmpty()) {
//            String uploadDir = new File(this.uploadDir).getAbsolutePath();
//            new File(uploadDir).mkdirs();
//
//            String fileName = user.getUsername() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
//            Path filePath = Paths.get(uploadDir, fileName);
//            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//            user.setProfilePictureUrl(fileName); // Or use uploadAccessUrl + fileName if needed
//        }
//
//        userRepository.save(user);
//        return mapToUserResponse(user);
//    }
    
    @Override
    public UserResponse registerUser(UserRequest request) throws IOException {
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

//        if (file != null && !file.isEmpty()) {
//            String uploadDir = new File(this.uploadDir).getAbsolutePath();
//            new File(uploadDir).mkdirs();
//
//            String fileName = user.getUsername() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
//            Path filePath = Paths.get(uploadDir, fileName);
//            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//            user.setProfilePictureUrl(fileName);
//        }
        
        if (file != null && !file.isEmpty()) {
            String uploadDir = new File(this.uploadDir).getAbsolutePath();
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = user.getEmail() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            user.setProfilePictureUrl(fileName);
        }

        userRepository.save(user);
        return mapToUserResponse(user);
    }

    @Override
    public UserResponse registerAdmin(UserRequest userRequest) {
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
    public UserResponse updateProfilePicture(String username, MultipartFile file) throws IOException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", username));

        String uploadDir = new File(this.uploadDir).getAbsolutePath();
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = username + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        //user.setProfilePictureUrl(uploadAccessUrl + fileName);
        user.setProfilePictureUrl(fileName);
        userRepository.save(user);

        return mapToUserResponse(user);
    }


    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    // ✅ Reusable helper methods
    private UserResponse mapToUserResponse(User user) {
        UserResponse response = modelMapper.map(user, UserResponse.class);
        response.setRoles(user.getRoles()); // ensure roles are set
        return response;
    }
}