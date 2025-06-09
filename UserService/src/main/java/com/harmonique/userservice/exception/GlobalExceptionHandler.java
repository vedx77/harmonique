package com.harmonique.userservice.exception;

import com.harmonique.userservice.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler to catch and handle various exceptions
 * across the whole User Service REST API in a centralized manner.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle ResourceNotFoundException thrown when a specific resource is missing.
     *
     * @param ex ResourceNotFoundException instance.
     * @return ResponseEntity with custom ApiResponse and HTTP 404.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        String message = ex.getMessage();
        ApiResponse response = ApiResponse.builder()
                .message(message)
                .success(false)
                .status(HttpStatus.NOT_FOUND.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle all uncaught or generic exceptions.
     *
     * @param ex Exception instance.
     * @return ResponseEntity with a generic error message and HTTP 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> genericExceptionHandler(Exception ex) {
        ApiResponse response = ApiResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle EmailAlreadyExistsException when trying to register a duplicate email.
     *
     * @param ex EmailAlreadyExistsException instance.
     * @return ResponseEntity with conflict status and custom message.
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        ApiResponse response = ApiResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.CONFLICT.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Handle UsernameAlreadyExistsException when trying to register a duplicate username.
     *
     * @param ex UsernameAlreadyExistsException instance.
     * @return ResponseEntity with conflict status and custom message.
     */
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex) {
        ApiResponse response = ApiResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.CONFLICT.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}