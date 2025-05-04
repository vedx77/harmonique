package com.harmonique.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harmonique.userservice.payload.JwtAuthRequest;
import com.harmonique.userservice.payload.JwtAuthResponse;
import com.harmonique.userservice.payload.UserRequest;
import com.harmonique.userservice.payload.UserResponse;
import com.harmonique.userservice.security.JwtAuthenticationFilter;
import com.harmonique.userservice.security.JwtTokenHelper;
import com.harmonique.userservice.service.UserService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private JwtTokenHelper jwtTokenHelper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Test
    void testRegisterUser() throws Exception {
        UserRequest userRequest = new UserRequest();
        // Fill dummy data if needed

        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setEmail("test@example.com");

        Mockito.when(userService.registerUser(any(UserRequest.class)))
                .thenReturn(userResponse);

        mockMvc.perform(post("/api/auth/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testRegisterAdminWithValidSecret() throws Exception {
        UserRequest userRequest = new UserRequest();
        // Fill dummy data if needed

        UserResponse userResponse = new UserResponse();
        userResponse.setId(2L);
        userResponse.setEmail("admin@example.com");

        Mockito.when(userService.registerAdmin(any(UserRequest.class)))
                .thenReturn(userResponse);

        mockMvc.perform(post("/api/auth/admin/register")
                        .param("adminSecret", "H@rmonique-2025")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.email").value("admin@example.com"));
    }

    @Test
    void testRegisterAdminWithInvalidSecret() throws Exception {
        UserRequest userRequest = new UserRequest();

        mockMvc.perform(post("/api/auth/admin/register")
                        .param("adminSecret", "wrong-secret")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testLogin() throws Exception {
        JwtAuthRequest loginRequest = new JwtAuthRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        JwtAuthResponse jwtResponse = new JwtAuthResponse();
        jwtResponse.setToken("mock-jwt-token");

        Mockito.when(userService.login(any(JwtAuthRequest.class)))
                .thenReturn(jwtResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }
}