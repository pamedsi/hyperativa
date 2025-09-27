package com.hyperativa.card.domain;


import org.junit.jupiter.api.Test;
import com.hyperativa.user.domain.User;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardTest {

    @Test
    void card_shouldCreateInstanceWithCorrectValuesWhenValidParametersProvided() {
        // Given
        User owner = mock(User.class);
        String hashedCardNumber = "hashed-card-number-123";
        String encryptedCardNumber = "encrypted-card-number-456";

        // When
        Card card = new Card(owner, hashedCardNumber, encryptedCardNumber);

        // Then
        assertNotNull(card.getIdentifier());
        assertEquals(hashedCardNumber, card.getCardNumberHash());
        assertEquals(encryptedCardNumber, card.getEncryptedCardNumber());
        assertEquals(owner, card.getOwner());
        assertNotNull(card.getSavedAt());
        assertTrue(card.getSavedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(card.getSavedAt().isAfter(LocalDateTime.now().minusSeconds(1)));
    }

    @Test
    void card_shouldGenerateUniqueIdentifierWhenNewInstanceIsCreated() {
        // Given
        User owner = mock(User.class);
        String hashedCardNumber = "hash-123";
        String encryptedCardNumber = "encrypted-456";

        // When
        Card card1 = new Card(owner, hashedCardNumber, encryptedCardNumber);
        Card card2 = new Card(owner, "different-hash", "different-encrypted");

        // Then
        assertNotNull(card1.getIdentifier());
        assertNotNull(card2.getIdentifier());
        assertNotEquals(card1.getIdentifier(), card2.getIdentifier());
    }
}