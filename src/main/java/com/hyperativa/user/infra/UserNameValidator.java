package com.hyperativa.user.infra;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserNameValidator implements ConstraintValidator<ValidUserName, String> {

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        try {
            String[] names = name.split(" ");
            return names.length >= 2;
        }
        catch (Exception e) {
            return false;
        }
    }

}
