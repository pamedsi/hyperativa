package com.hyperativa.user.infra;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.YEARS;

public class BirthdateValidator implements ConstraintValidator<ValidBirthdate, LocalDate> {

    private static final int ADULT_AGE = 18;

    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext constraintValidatorContext) {
        if (birthdate == null) return false;
        LocalDate now = LocalDate.now();
        return YEARS.between(birthdate, now) >= ADULT_AGE;
    }

}
