package com.gmail.derynem.service.exception;

public class ReviewServiceException extends RuntimeException {
    public ReviewServiceException() {
        super();
    }

    public ReviewServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
