package com.hyperativa.user.infra;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BirthdateValidatorTest {

    private final BirthdateValidator validator = new BirthdateValidator();

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    void initiateDependencies() {
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addConstraintViolation()).thenReturn(context);
    }

    @Test
    void isValid_shouldReturnFalse_whenBirthdateIsNull() {
        // Given
        initiateDependencies();

        // When
        boolean result = validator.isValid(null, context);

        // Then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate(contains("Birthdate is required"));

    }

    @Test
    void isValid_shouldReturnFalse_whenUserIsUnder18YearsOld() {
        // Given
        String date = getDateInFormat(17);
        initiateDependencies();

        // When
        boolean result = validator.isValid(date, context);

        // Then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate(contains("User must be at least 18 old to save cards"));
    }

    @Test
    void isValid_shouldReturnFalse_whenInvalidFormat() {
        // Given
        String date = "26/03/1999";
        initiateDependencies();

        // When
        boolean result = validator.isValid(date, context);

        // Then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate(contains("Birthdate format is invalid! Use MM/dd/yyyy"));
    }

    @Test
    void isValid_shouldReturnTrue_whenUserIsOver18YearsOld() {
        // Given
        String date = getDateInFormat(18);

        // When
        boolean result = validator.isValid(date, context);

        // Then
        assertTrue(result);
    }

    private String getDateInFormat(int yearsAgo) {
        return LocalDate.now().minusYears(yearsAgo).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

}