package com.harmonique.userservice.exception;

/**
 * Custom exception thrown when trying to register or update a user
 * with an email that already exists in the system.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs the exception with a custom message.
     *
     * @param message Detailed error message explaining the conflict.
     */
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}