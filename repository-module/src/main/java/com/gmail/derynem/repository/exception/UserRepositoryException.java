package com.gmail.derynem.repository.exception;

public class UserRepositoryException extends RuntimeException {
    public UserRepositoryException() {
        super();
    }

    public UserRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
