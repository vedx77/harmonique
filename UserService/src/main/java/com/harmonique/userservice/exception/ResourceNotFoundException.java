package com.harmonique.userservice.exception;

/**
 * Custom exception thrown when a specific resource is not found in the system.
 * 
 * Commonly used for entities like User, Role, etc.
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor with a generic message.
     */
    public ResourceNotFoundException() {
        super("Resource not found on server !!");
    }

    /**
     * Constructor with a custom error message.
     *
     * @param message The error message to return.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor with dynamic message based on resource details.
     *
     * @param resourceName Name of the resource (e.g., "User")
     * @param fieldName    The field used to search (e.g., "ID")
     * @param fieldValue   The value of the field (e.g., 123)
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}