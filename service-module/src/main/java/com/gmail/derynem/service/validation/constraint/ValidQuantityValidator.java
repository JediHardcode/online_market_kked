package com.gmail.derynem.service.validation.constraint;

import com.gmail.derynem.service.validation.annotation.ValidQuantity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.gmail.derynem.service.validation.constant.ValidationConstant.ONLY_DIGITS;

public class ValidQuantityValidator implements ConstraintValidator<ValidQuantity, String> {
    public boolean isValid(String quantity, ConstraintValidatorContext context) {
        if (!quantity.matches(ONLY_DIGITS)) {
            return false;
        } else return Long.valueOf(quantity) >= 1;
    }
}