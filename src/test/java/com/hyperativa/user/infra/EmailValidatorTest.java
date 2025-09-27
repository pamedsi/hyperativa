package com.hyperativa.user.infra;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hyperativa.user.application.repository.UserRepository;

import jakarta.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailValidatorTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConstraintValidatorContext context;

    @InjectMocks
    private EmailValidator emailValidator;

    @Test
    void isValid_shouldReturnFalse_whenEmailAlreadyExistsInRepository() {
        // Given
        String existingEmail = "existing@example.com";
        when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

        // When
        boolean result = emailValidator.isValid(existingEmail, context);

        // Then
        assertFalse(result);
        verify(userRepository, times(1)).existsByEmail(existingEmail);
    }

    @Test
    void isValid_shouldReturnTrue_whenEmailDoesNotExistInRepository() {
        // Given
        String newEmail = "newuser@example.com";
        when(userRepository.existsByEmail(newEmail)).thenReturn(false);

        // When
        boolean result = emailValidator.isValid(newEmail, context);

        // Then
        assertTrue(result);
        verify(userRepository, times(1)).existsByEmail(newEmail);
    }

    @Test
    void isValid_shouldReturnFalse_whenEmailIsNull() {
        // Given
        String nullEmail = null;

        // When
        boolean result = emailValidator.isValid(nullEmail, context);

        // Then
        assertFalse(result);
        verifyNoInteractions(userRepository);
    }

    @Test
    void isValid_shouldReturnFalse_whenEmailIsBlank() {
        // Given
        String blankEmail = "   ";

        // When
        boolean result = emailValidator.isValid(blankEmail, context);

        // Then
        assertFalse(result);
        verifyNoInteractions(userRepository);
    }

    @Test
    void isValid_shouldReturnFalse_whenRepositoryThrowsException() {
        // Given
        String validEmail = "test@example.com";
        when(userRepository.existsByEmail(validEmail))
                .thenThrow(new RuntimeException("Database error"));

        // When
        boolean result = emailValidator.isValid(validEmail, context);

        // Then
        assertFalse(result);
        verify(userRepository, times(1)).existsByEmail(validEmail);
    }

}