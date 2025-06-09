package com.harmonique.userservice.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ApiResponse is a reusable structure for sending messages back to clients.
 * It includes a message, success flag, and HTTP status code.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse {

    private String message;   // Message for client

    private boolean success;  // Flag indicating success/failure

    private int status;       // HTTP status code
}