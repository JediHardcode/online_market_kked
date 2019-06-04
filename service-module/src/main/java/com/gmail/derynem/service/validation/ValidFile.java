package com.gmail.derynem.service.validation;

import com.gmail.derynem.service.validation.constraint.ValidFileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidFileValidator.class)
public @interface ValidFile {
    String message() default "{file.not.valid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}