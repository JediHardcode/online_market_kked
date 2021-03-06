package com.gmail.derynem.service.validation.constraint;

import com.gmail.derynem.service.UserService;
import com.gmail.derynem.service.model.user.UserDTO;
import com.gmail.derynem.service.validation.annotation.UniqueEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserService userService;

    public UniqueEmailValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        UserDTO userDTO = userService.getUserByEmail(email, null);
        return userDTO == null;
    }
}