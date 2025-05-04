package com.harmonique.userservice.controller;

import com.harmonique.userservice.entity.Role;
import com.harmonique.userservice.payload.UserResponse;
import com.harmonique.userservice.security.JwtAuthenticationFilter;
import com.harmonique.userservice.security.JwtTokenHelper;
import com.harmonique.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Set;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    
    @MockBean
    private JwtTokenHelper jwtTokenHelper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Test
    @WithMockUser(username = "testuser")
    void testGetUserProfile() throws Exception {
        // Prepare mocked service response
    	UserResponse mockResponse = UserResponse.builder()
    	        .id(1L)
    	        .name("testuser")
    	        .email("testuser@example.com")
    	        .about("Sample about text")
    	        .roles(Set.of(new Role(1L, "ROLE_USER")))
    	        .build();

        Mockito.when(userService.getUserProfile(anyString())).thenReturn(mockResponse);

        // Perform GET /api/user/profile
//        mockMvc.perform(get("/api/user/profile"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("testuser"))
//                .andExpect(jsonPath("$.email").value("testuser@example.com"))
//                .andExpect(jsonPath("$.roles[0].name").value("ROLE_USER"));
        
        mockMvc.perform(get("/api/user/profile"))
        .andExpect(status().isOk())
        .andDo(print());  // ← this prints the raw response body
    }
}
