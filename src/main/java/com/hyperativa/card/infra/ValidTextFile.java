package com.hyperativa.card.infra;

import com.auth0.jwt.interfaces.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidTextFileValidator.class)
public @interface ValidTextFile {

    String message() default "Invalid file";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    long maxSize() default 5 * 1024 * 1024; // 5MB default
    String[] allowedTypes() default {"text/plain", "text/html"};

}