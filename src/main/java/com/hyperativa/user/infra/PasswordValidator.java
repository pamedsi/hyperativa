package com.hyperativa.user.infra;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-+";
    private static final String DIGITS = "0123456789";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final int MIN_LENGTH = 10;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (!isBasicRequirementsMet(password)) {
            return false;
        }
        return hasRequiredCharacterTypes(password);
    }

    private boolean isBasicRequirementsMet(String password) {
        return password != null && !password.isBlank() && password.length() >= MIN_LENGTH;
    }

    private boolean hasRequiredCharacterTypes(String password) {
        boolean hasSpecial = containsAnyChar(password, SPECIAL_CHARACTERS);
        boolean hasDigit = containsAnyChar(password, DIGITS);
        boolean hasLower = containsAnyChar(password, LOWERCASE);
        boolean hasUpper = containsAnyChar(password, UPPERCASE);

        return hasSpecial && hasDigit && hasLower && hasUpper;
    }

    private boolean containsAnyChar(String password, String charSet) {
        return password.chars().anyMatch(c -> charSet.indexOf(c) >= 0);
    }

}
