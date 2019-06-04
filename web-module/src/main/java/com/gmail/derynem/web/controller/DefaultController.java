package com.gmail.derynem.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.gmail.derynem.web.constants.PageNamesConstant.CUSTOM_ERROR;
import static com.gmail.derynem.web.constants.PageNamesConstant.ERROR_PAGE_FORBIDDEN;
import static com.gmail.derynem.web.constants.PageNamesConstant.ERROR_PAGE_NOT_FOUND;
import static com.gmail.derynem.web.constants.PageNamesConstant.LOGIN_PAGE;

@Controller
public class DefaultController {

    @GetMapping("/login")
    public String login() {
        return LOGIN_PAGE;
    }

    @GetMapping("/403")
    public String errorAccessDenied() {
        return ERROR_PAGE_FORBIDDEN;
    }

    @GetMapping("/404")
    public String errorNotFound() {
        return ERROR_PAGE_NOT_FOUND;
    }

    @GetMapping("/500")
    public String serverEternalError() {
        return CUSTOM_ERROR;
    }
}