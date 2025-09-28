package com.hyperativa.handler;

import jakarta.validation.ConstraintValidatorContext;

public abstract class CustomMessageBuilder {

    private CustomMessageBuilder() {
        // Prevent instantiation
    }

    public static void buildCustomMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }

}
