package com.gmail.derynem.service.model.api;

import java.time.LocalDateTime;

public class ResponseDTO {
    private String message;
    private String date;
    private String errorCode;

    public ResponseDTO() {
        date = String.valueOf(LocalDateTime.now());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}