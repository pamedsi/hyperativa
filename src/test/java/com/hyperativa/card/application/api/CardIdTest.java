package com.hyperativa.card.application.api;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CardIdTest {

    @Test
    void cardId_shouldCreateInstanceWithValidUUIDWhenProvided() {
        // Given
        UUID expectedUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        // When
        CardId cardId = new CardId(expectedUUID);

        // Then
        assertNotNull(cardId);
        assertEquals(expectedUUID, cardId.cardIdentifier());
    }

    @Test
    void cardId_shouldReturnSameUUIDWhenAccessingCardIdentifier() {
        // Given
        UUID randomUUID = UUID.randomUUID();
        CardId cardId = new CardId(randomUUID);

        // When
        UUID result = cardId.cardIdentifier();

        // Then
        assertEquals(randomUUID, result);
    }
}