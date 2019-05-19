package com.gmail.derynem.repository.exception;

public class GenericRepositoryException extends RuntimeException {
    public GenericRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}