package com.gmail.derynem.service.exception;

public class ItemServiceException extends Exception {
    public ItemServiceException(String message) {
        super(message);
    }

    public ItemServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}