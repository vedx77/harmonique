package com.harmonique.userservice.exception;

import com.harmonique.userservice.payload.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testResourceNotFoundExceptionHandler() {
        String errorMessage = "User not found";
        ResourceNotFoundException ex = new ResourceNotFoundException(errorMessage);

        ResponseEntity<ApiResponse> responseEntity = handler.resourceNotFoundExceptionHandler(ex);
        ApiResponse response = responseEntity.getBody();

        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());

        assertNotNull(response);
        assertEquals(errorMessage, response.getMessage());
        assertFalse(response.isSuccess());
        assertEquals(404, response.getStatus());
    }

    @Test
    void testGenericExceptionHandler() {
        String errorMessage = "Internal server error";
        Exception ex = new Exception(errorMessage);

        ResponseEntity<ApiResponse> responseEntity = handler.genericExceptionHandler(ex);
        ApiResponse response = responseEntity.getBody();

        assertNotNull(responseEntity);
        assertEquals(500, responseEntity.getStatusCodeValue());

        assertNotNull(response);
        assertEquals(errorMessage, response.getMessage());
        assertFalse(response.isSuccess());
        assertEquals(500, response.getStatus());
    }
}
