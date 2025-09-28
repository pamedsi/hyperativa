package com.hyperativa.user.infra;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.hyperativa.handler.CustomMessageBuilder.buildCustomMessage;
import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.YEARS;

public class BirthdateValidator implements ConstraintValidator<ValidBirthdate, String> {

    private static final int ADULT_AGE = 18;

    @Override
    public boolean isValid(String birthdate, ConstraintValidatorContext context) {
        if (birthdate == null || birthdate.trim().isEmpty()) {
            buildCustomMessage(context, "Birthdate is required");
            return false;
        }

        LocalDate birthdateParsed;
        try {
            birthdateParsed = LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        } catch (Exception e) {
            buildCustomMessage(context, "Birthdate format is invalid! Use MM/dd/yyyy");
            return false;
        }

        if (YEARS.between(birthdateParsed, now()) < ADULT_AGE) {
            buildCustomMessage(context, "User must be at least 18 old to save cards");
            return false;
        }

        return true;
    }

}
