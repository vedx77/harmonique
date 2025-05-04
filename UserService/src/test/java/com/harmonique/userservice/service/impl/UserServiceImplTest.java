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
import com.harmonique.userservice.security.JwtTokenHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private JwtTokenHelper jwtTokenHelper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("John");
        userRequest.setEmail("john@example.com");
        userRequest.setPassword("pass123");
        userRequest.setAbout("About John");

        Role userRole = Role.builder().id(1L).name("USER").build();
        User savedUser = User.builder().id(1L).name("John").email("john@example.com").password("encodedPass").about("About John").roles(new HashSet<>(Set.of(userRole))).build();
        UserResponse userResponse = new UserResponse();

        when(roleRepository.findById(1L)).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode("pass123")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(modelMapper.map(any(User.class), eq(UserResponse.class))).thenReturn(userResponse);

        UserResponse result = userService.registerUser(userRequest);

        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserByEmail_NotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserByEmail("notfound@example.com");
        });
    }

    @Test
    void testLogin_Success() {
        JwtAuthRequest authRequest = new JwtAuthRequest("john@example.com", "pass123");
        Role userRole = Role.builder().id(1L).name("USER").build();
        User user = User.builder().id(1L).email("john@example.com").password("encodedPass").roles(new HashSet<>(Set.of(userRole))).build();

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass123", "encodedPass")).thenReturn(true);
        when(jwtTokenHelper.generateToken(any())).thenReturn("mockToken");
        when(modelMapper.map(any(User.class), eq(UserResponse.class))).thenReturn(new UserResponse());

        JwtAuthResponse response = userService.login(authRequest);

        assertNotNull(response);
        assertEquals("mockToken", response.getToken());
    }

    @Test
    void testLogin_BadCredentials() {
        JwtAuthRequest authRequest = new JwtAuthRequest("john@example.com", "wrongPass");
        User user = User.builder().id(1L).email("john@example.com").password("encodedPass").build();

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> {
            userService.login(authRequest);
        });
    }
}