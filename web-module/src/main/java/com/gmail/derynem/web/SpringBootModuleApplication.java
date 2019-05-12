package com.gmail.derynem.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.gmail.derynem.service",
        "com.gmail.derynem.repository",
        "com.gmail.derynem.web"},
        exclude = UserDetailsServiceAutoConfiguration.class)
public class SpringBootModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootModuleApplication.class, args);
    }
}
