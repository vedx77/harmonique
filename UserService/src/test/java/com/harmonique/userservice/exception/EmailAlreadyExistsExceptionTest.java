package com.harmonique.userservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailAlreadyExistsExceptionTest {

    @Test
    void testExceptionMessage() {
        String testMessage = "Email already exists";
        
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException(testMessage);
        
        assertNotNull(exception, "Exception instance should not be null");
        assertEquals(testMessage, exception.getMessage(), "Exception message should match the input message");
    }
}