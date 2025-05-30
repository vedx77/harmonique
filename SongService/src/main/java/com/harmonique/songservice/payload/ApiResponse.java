package com.harmonique.songservice.payload;

import lombok.*;
import org.springframework.http.HttpStatus;

/**
 * Generic API response structure for all endpoints.
 * Used to standardize success/failure messages across the service.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse {

    private String message;       // Message describing the response
    private boolean success;      // Indicates if the operation was successful
    private HttpStatus status;   // HTTP status code associated with the response
}