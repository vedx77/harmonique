package com.harmonique.userservice.exception;

import com.harmonique.userservice.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle Resource Not Found Exception
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

    // Handle Generic Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> genericExceptionHandler(Exception ex) {
        ApiResponse response = ApiResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        ApiResponse response = ApiResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.CONFLICT.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex) {
        ApiResponse response = ApiResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.CONFLICT.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MissingContactInformationException.class)
    public ResponseEntity<ApiResponse> handleMissingContact(MissingContactInformationException ex) {
        ApiResponse response = ApiResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}