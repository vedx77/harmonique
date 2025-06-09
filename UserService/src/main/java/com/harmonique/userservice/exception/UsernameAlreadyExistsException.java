package com.harmonique.userservice.exception;

/**
 * Custom exception thrown when trying to register or update a user
 * with a username that already exists in the system.
 */
public class UsernameAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs the exception with a custom message.
     *
     * @param message Detailed error message explaining the conflict.
     */
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}