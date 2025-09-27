package com.hyperativa.card.application.api;

import com.hyperativa.auth.application.service.TokenService;
import com.hyperativa.card.application.service.CardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardRestControllerTest {

    @Mock
    private CardService cardService;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private CardRestController cardRestController;

    @Test
    void addSingleCard_shouldDecodeTokenAndCallServiceWhenValidInput() {
        // Given
        CardNumber cardNumber = new CardNumber("1234567890123456");
        String token = "valid-token";
        String decodedEmail = "user@email.com";

        when(tokenService.decode(token)).thenReturn(decodedEmail);

        // When
        cardRestController.addSingleCard(cardNumber, token);

        // Then
        verify(tokenService, times(1)).decode(token);
        verify(cardService, times(1)).addSingleCard(cardNumber, decodedEmail);
    }

    @Test
    void addSingleCard_shouldHandleValidCardNumberFormatWhenAddingCard() {
        // Given
        CardNumber cardNumber = new CardNumber("4111111111111111");
        String token = "bearer-token";
        String decodedEmail = "owner@domain.com";

        when(tokenService.decode(token)).thenReturn(decodedEmail);

        // When
        cardRestController.addSingleCard(cardNumber, token);

        // Then
        verify(cardService, times(1)).addSingleCard(cardNumber, decodedEmail);
    }

    @Test
    void addSingleCard_shouldPropagateExceptionWhenTokenIsInvalid() {
        // Given
        CardNumber cardNumber = new CardNumber("1234567890123456");
        String invalidToken = "invalid-token";

        when(tokenService.decode(invalidToken))
                .thenThrow(new RuntimeException("Invalid token"));

        // When & Then
        assertThrows(RuntimeException.class, () ->
                cardRestController.addSingleCard(cardNumber, invalidToken));

        verify(cardService, times(0)).addSingleCard(any(), anyString());
    }
}