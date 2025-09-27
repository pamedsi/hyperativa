package com.hyperativa.user.infra;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserNameValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    @Test
    void isValid_shouldReturnTrueWhenNameHasAtLeastTwoWords() {
        // Given
        UserNameValidator validator = new UserNameValidator();
        String validName = "John Doe";

        // When
        boolean result = validator.isValid(validName, context);

        // Then
        assertTrue(result);
    }

    @Test
    void isValid_shouldReturnFalseWhenNameHasOnlyOneWord() {
        // Given
        UserNameValidator validator = new UserNameValidator();
        String invalidName = "John";

        // When
        boolean result = validator.isValid(invalidName, context);

        // Then
        assertFalse(result);
    }

    @Test
    void isValid_shouldReturnFalseWhenNameIsNull() {
        // Given
        UserNameValidator validator = new UserNameValidator();
        String nullName = null;

        // When
        boolean result = validator.isValid(nullName, context);

        // Then
        assertFalse(result);
    }

    @Test
    void isValid_shouldReturnTrueWhenNameHasMoreThanTwoWords() {
        // Given
        UserNameValidator validator = new UserNameValidator();
        String validName = "John Michael Doe";

        // When
        boolean result = validator.isValid(validName, context);

        // Then
        assertTrue(result);
    }
}