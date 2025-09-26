package com.hyperativa.user.infra;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserNameValidator.class)
public @interface ValidUserName {

    String message() default "User must have at least 2 names. Ex: Henry Kane";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
