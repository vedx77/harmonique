package com.harmonique.userservice.controller;

import com.harmonique.userservice.payload.UserResponse;
import com.harmonique.userservice.security.JwtAuthenticationFilter;
import com.harmonique.userservice.security.JwtTokenHelper;
import com.harmonique.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
	@MockBean
    private UserService userService;
    
    @SuppressWarnings("removal")
	@MockBean
    private JwtTokenHelper jwtTokenHelper;

    @SuppressWarnings("removal")
	@MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @SuppressWarnings("unused")
	private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetUserByEmail() throws Exception {
        // Arrange
        String email = "admin@example.com";
        UserResponse mockUser = UserResponse.builder()
                .id(1L)
                .firstName("Admin User")
                .email(email)
                .build();

        when(userService.getUserByEmail(email)).thenReturn(mockUser);

        // Act & Assert
        mockMvc.perform(get("/api/admin/user/email/{email}", email)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Admin User"))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void testGetAllUsers() throws Exception {
        // Arrange
        UserResponse user1 = UserResponse.builder().id(1L).firstName("User One").email("one@example.com").build();
        UserResponse user2 = UserResponse.builder().id(2L).firstName("User Two").email("two@example.com").build();
        List<UserResponse> mockUsers = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(mockUsers);

        // Act & Assert
        mockMvc.perform(get("/api/admin/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("User One"))
                .andExpect(jsonPath("$[1].email").value("two@example.com"));
    }
}