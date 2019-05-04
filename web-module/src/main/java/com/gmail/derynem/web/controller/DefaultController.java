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
}
