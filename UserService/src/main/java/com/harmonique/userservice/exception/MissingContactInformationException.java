package com.harmonique.userservice.exception;

public class MissingContactInformationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MissingContactInformationException(String message) {
        super(message);
    }
}