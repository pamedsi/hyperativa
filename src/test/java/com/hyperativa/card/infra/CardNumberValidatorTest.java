package com.hyperativa.card.infra;

import com.hyperativa.card.application.repository.CardRepository;
import com.hyperativa.utils.Hasher;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardNumberValidatorTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private Hasher hasher;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    private CardNumberValidator createValidator() {
        return new CardNumberValidator(cardRepository, hasher);
    }

    @Test
    void isValid_shouldReturnFalseWhenCardNumberFailsLuhnCheck() {
        // Given
        CardNumberValidator validator = createValidator();
        String invalidLuhnNumber = "4111111111111112"; // Invalid Luhn number
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addConstraintViolation()).thenReturn(context);

        // When
        boolean result = validator.isValid(invalidLuhnNumber, context);

        // Then
        assertFalse(result);
        verify(context, times(1)).disableDefaultConstraintViolation();
        verify(context, times(1)).buildConstraintViolationWithTemplate(contains("is not a valid card number"));
    }
}