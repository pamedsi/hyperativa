package com.hyperativa.user.infra;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BirthdateValidatorTest {

    private final BirthdateValidator validator = new BirthdateValidator();

    @Mock
    private ConstraintValidatorContext context;

    @Test
    void isValid_shouldReturnFalse_whenBirthdateIsNull() {
        // Given
        LocalDate nullBirthdate = null;

        // When
        boolean result = validator.isValid(nullBirthdate, context);

        // Then
        assertFalse(result);
    }

    @Test
    void isValid_shouldReturnFalse_whenUserIsUnder18YearsOld() {
        // Given
        LocalDate underageBirthdate = LocalDate.now().minusYears(17);

        // When
        boolean result = validator.isValid(underageBirthdate, context);

        // Then
        assertFalse(result);
    }

    @Test
    void isValid_shouldReturnFalse_whenUserIsExactly17YearsAnd364DaysOld() {
        // Given
        LocalDate almost18Birthdate = LocalDate.now().minusYears(18).plusDays(1);

        // When
        boolean result = validator.isValid(almost18Birthdate, context);

        // Then
        assertFalse(result);
    }

    @Test
    void isValid_shouldReturnTrue_whenUserIsExactly18YearsOld() {
        // Given
        LocalDate exactly18Birthdate = LocalDate.now().minusYears(18);

        // When
        boolean result = validator.isValid(exactly18Birthdate, context);

        // Then
        assertTrue(result);
    }

    @Test
    void isValid_shouldReturnTrue_whenUserIsOver18YearsOld() {
        // Given
        LocalDate adultBirthdate = LocalDate.now().minusYears(25);

        // When
        boolean result = validator.isValid(adultBirthdate, context);

        // Then
        assertTrue(result);
    }

    @Test
    void isValid_shouldReturnTrue_whenUserIs65YearsOld() {
        // Given
        LocalDate seniorBirthdate = LocalDate.now().minusYears(65);

        // When
        boolean result = validator.isValid(seniorBirthdate, context);

        // Then
        assertTrue(result);
    }
}