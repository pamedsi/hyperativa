package com.hyperativa.card.infra;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;

import com.hyperativa.card.application.repository.CardJPARepository;
import com.hyperativa.card.domain.Card;
import com.hyperativa.handler.exceptions.APIException;
import com.hyperativa.handler.exceptions.CardNotFoundException;
import com.hyperativa.user.domain.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardInfraRepositoryTest {

    @Mock
    private CardJPARepository cardJPARepository;

    @InjectMocks
    private CardInfraRepository cardInfraRepository;

    @Test
    void save_shouldThrowAPIException_whenOptimisticLockingFailureExceptionOccurs() {
        // Given
        Card card = mock(Card.class);
        when(cardJPARepository.save(card)).thenThrow(new OptimisticLockingFailureException("Lock error"));

        // When & Then
        APIException exception = assertThrows(APIException.class, () -> cardInfraRepository.save(card));

        assertEquals("Error saving card!", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
        verify(cardJPARepository, times(1)).save(card);
    }

    @Test
    void getCardByOwner_shouldReturnCard_whenCardExists() {
        // Given
        User owner = new User();
        String hashCardNumber = "hashedCardNumber123";
        Card expectedCard = mock(Card.class);

        when(cardJPARepository.findByOwnerAndCardNumberHash(owner, hashCardNumber))
                .thenReturn(Optional.of(expectedCard));

        // When
        Card result = cardInfraRepository.getCardByOwner(owner, hashCardNumber);

        // Then
        assertNotNull(result);
        assertEquals(expectedCard, result);
        verify(cardJPARepository, times(1))
                .findByOwnerAndCardNumberHash(owner, hashCardNumber);
    }

    @Test
    void getCardByOwner_shouldThrowCardNotFoundException_whenCardDoesNotExist() {
        // Given
        User owner = new User();
        String hashCardNumber = "nonExistentHash";

        when(cardJPARepository.findByOwnerAndCardNumberHash(owner, hashCardNumber))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(CardNotFoundException.class, () -> cardInfraRepository.getCardByOwner(owner, hashCardNumber));

        verify(cardJPARepository, times(1))
                .findByOwnerAndCardNumberHash(owner, hashCardNumber);
    }

    @Test
    void saveAll_shouldThrowAPIException_whenDataIntegrityViolationExceptionOccurs() {
        // Given
        List<Card> cards = Arrays.asList(mock(Card.class), mock(Card.class));
        when(cardJPARepository.saveAll(cards))
                .thenThrow(new DataIntegrityViolationException("Duplicate card number"));

        // When & Then
        APIException exception = assertThrows(APIException.class, () -> cardInfraRepository.saveAll(cards));

        assertEquals("One of the card numbers already exists!", exception.getMessage());
        assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
        verify(cardJPARepository, times(1)).saveAll(cards);
    }

    @Test
    void existsCardByNumber_shouldReturnTrue_whenCardExists() {
        // Given
        String cardNumberHash = "existingCardHash";
        when(cardJPARepository.existsByCardNumberHash(cardNumberHash)).thenReturn(true);

        // When
        boolean result = cardInfraRepository.existsCardByNumber(cardNumberHash);

        // Then
        assertTrue(result);
        verify(cardJPARepository, times(1)).existsByCardNumberHash(cardNumberHash);
    }

    @Test
    void existsCardByNumber_shouldReturnFalse_whenIllegalArgumentExceptionOccurs() {
        // Given
        String cardNumberHash = "invalidHash";
        when(cardJPARepository.existsByCardNumberHash(cardNumberHash))
                .thenThrow(new IllegalArgumentException("Invalid hash"));

        // When
        boolean result = cardInfraRepository.existsCardByNumber(cardNumberHash);

        // Then
        assertFalse(result);
        verify(cardJPARepository, times(1)).existsByCardNumberHash(cardNumberHash);
    }
}