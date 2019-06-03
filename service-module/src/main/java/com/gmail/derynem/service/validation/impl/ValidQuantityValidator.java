package com.gmail.derynem.service.validation.impl;

import com.gmail.derynem.service.validation.ValidQuantity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.gmail.derynem.service.validation.constant.ValidationConstant.ONLY_DIGITS;

public class ValidQuantityValidator implements ConstraintValidator<ValidQuantity, String> {
    public void initialize(ValidQuantity constraint) {
    }

    public boolean isValid(String quantity, ConstraintValidatorContext context) {
        if (!quantity.matches(ONLY_DIGITS)) {
            return false;
        } else return Long.valueOf(quantity) >= 1;
    }
}