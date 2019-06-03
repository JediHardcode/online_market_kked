package com.gmail.derynem.web.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackages = "com.gmail.derynem.web.api")
public class RestExceptionHandler {
    private final static Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<?> handleControllerException(HttpServletRequest request, Exception e) {
        logger.error(e.getMessage(), e);
        HttpStatus status = getStatus(request);
        return new ResponseEntity<>(e.getMessage(), status);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}