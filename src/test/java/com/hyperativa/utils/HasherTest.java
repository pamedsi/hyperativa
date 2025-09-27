package com.hyperativa.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HasherTest {

    @Test
    void hashCardNumber_shouldReturnConsistentSHA256Hash_whenSameInputProvided() {
        // Given
        Hasher hasher = new Hasher("test-salt-123");
        String cardNumber = "1234567890123456";

        // When
        String result1 = hasher.hashCardNumber(cardNumber);
        String result2 = hasher.hashCardNumber(cardNumber);

        // Then
        assertNotNull(result1);
        assertEquals(64, result1.length()); // SHA256 produces 64-character hex string
        assertEquals(result1, result2); // Should be consistent for same input
        assertTrue(result1.matches("^[a-f0-9]{64}$")); // Valid SHA256 hex format
    }

    @Test
    void hashPassword_shouldReturnDifferentBCryptHash_whenSamePasswordHashedMultipleTimes() {
        // Given
        Hasher hasher = new Hasher("test-salt-123");
        String password = "MySecurePassword123!";

        // When
        String result1 = hasher.hashPassword(password);
        String result2 = hasher.hashPassword(password);

        // Then
        assertNotNull(result1);
        assertTrue(result1.startsWith("$2a$")); // BCrypt format
        assertNotEquals(result1, result2); // Should be different due to random salt
        assertTrue(result1.length() > 50); // BCrypt hash length
    }
}