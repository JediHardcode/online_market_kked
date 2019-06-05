package com.gmail.derynem.web.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_CUSTOM_ERROR;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_NOT_FOUND;

@ControllerAdvice(basePackages = "com.gmail.derynem.web.controller")
public class AppExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(AppExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public String defaultErrorPage(Exception e) {
        logger.error(e.getMessage(), e);
        return REDIRECT_CUSTOM_ERROR;
    }
}