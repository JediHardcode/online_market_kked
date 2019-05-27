package com.gmail.derynem.service.validation.impl;

import com.gmail.derynem.service.UserService;
import com.gmail.derynem.service.model.user.UserDTO;
import com.gmail.derynem.service.validation.UniqueEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserService userService;

    public UniqueEmailValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        UserDTO userDTO = userService.getUserByEmail(email);
        return userDTO == null;
    }
}