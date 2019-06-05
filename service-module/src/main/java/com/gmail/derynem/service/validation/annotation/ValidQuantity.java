package com.gmail.derynem.service.validation.annotation;

import com.gmail.derynem.service.validation.constraint.ValidQuantityValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidQuantityValidator.class)
public @interface ValidQuantity {
    String message() default "{order.quantity.not.valid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}