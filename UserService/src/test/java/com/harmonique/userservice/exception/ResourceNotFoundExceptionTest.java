package com.harmonique.userservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void testDefaultConstructor() {
        ResourceNotFoundException exception = new ResourceNotFoundException();

        assertNotNull(exception);
        assertEquals("Resource not found on server !!", exception.getMessage());
    }

    @Test
    void testMessageConstructor() {
        String message = "User not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testResourceDetailsConstructor() {
        String resourceName = "User";
        String fieldName = "id";
        Long fieldValue = 42L;

        ResourceNotFoundException exception = new ResourceNotFoundException(resourceName, fieldName, fieldValue);

        String expectedMessage = "User not found with id : '42'";

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }
}