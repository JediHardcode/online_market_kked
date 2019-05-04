package com.gmail.derynem.service.exception;

public class UserServiceException extends RuntimeException {
    public UserServiceException() {
    }

    public UserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
