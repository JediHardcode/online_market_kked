package com.gmail.derynem.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.gmail.derynem.web.constants.PageNames.*;

@Controller
public class DefaultController {
    @GetMapping("/home")
    public String home() {
        return HOME_PAGE;
    }

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
}
