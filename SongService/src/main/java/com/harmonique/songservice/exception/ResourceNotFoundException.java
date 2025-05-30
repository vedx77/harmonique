package com.harmonique.songservice.exception;

/**
 * Custom exception thrown when a requested resource is not found.
 * Used for returning 404 Not Found responses.
 */
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    /**
     * Default constructor with a standard not-found message.
     */
    public ResourceNotFoundException() {
        super("Resource not found on server");
    }

    /**
     * Constructor that allows specifying a custom error message.
     *
     * @param message Custom error message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}