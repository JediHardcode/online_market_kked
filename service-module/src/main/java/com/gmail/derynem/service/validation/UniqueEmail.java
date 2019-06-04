package com.gmail.derynem.service.validation;

import com.gmail.derynem.service.validation.constraint.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {
    String message() default "{user.email.exist}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}