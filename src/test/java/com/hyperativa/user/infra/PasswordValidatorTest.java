package com.hyperativa.user.infra;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PasswordValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    @Test
    void isValid_shouldReturnTrueWhenPasswordMeetsAllRequirements() {
        // Given
        PasswordValidator validator = new PasswordValidator();
        String validPassword = "SecurePass123!"; // Tem especial, dígito, maiúscula, minúscula e >=10 chars

        // When
        boolean result = validator.isValid(validPassword, context);

        // Then
        assertTrue(result);
    }

    @Test
    void isValid_shouldReturnFalseWhenPasswordMissingSpecialCharacter() {
        // Given
        PasswordValidator validator = new PasswordValidator();
        String invalidPassword = "SecurePass123"; // Falta caractere especial

        // When
        boolean result = validator.isValid(invalidPassword, context);

        // Then
        assertFalse(result);
    }

    @Test
    void isValid_shouldReturnFalseWhenPasswordIsTooShort() {
        // Given
        PasswordValidator validator = new PasswordValidator();
        String shortPassword = "Pass123!"; // Apenas 8 caracteres

        // When
        boolean result = validator.isValid(shortPassword, context);

        // Then
        assertFalse(result);
    }

    @Test
    void isValid_shouldReturnFalseWhenPasswordIsNull() {
        // Given
        PasswordValidator validator = new PasswordValidator();

        // When
        boolean result = validator.isValid(null, context);

        // Then
        assertFalse(result);
    }

}