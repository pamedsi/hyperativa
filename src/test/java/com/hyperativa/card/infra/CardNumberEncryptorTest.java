package com.hyperativa.card.infra;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardNumberEncryptorTest {

    @Mock
    private StringEncryptor encryptor;

    @InjectMocks
    private CardNumberEncryptor cardNumberEncryptor;

    @Test
    void encrypt_shouldReturnEncryptedStringWhenValidCardNumberProvided() {
        // Given
        String cardNumber = "1234567890123456";
        String expectedEncrypted = "encrypted-card-number-123";

        when(encryptor.encrypt(cardNumber)).thenReturn(expectedEncrypted);

        // When
        String result = cardNumberEncryptor.encrypt(cardNumber);

        // Then
        assertEquals(expectedEncrypted, result);
        verify(encryptor, times(1)).encrypt(cardNumber);
    }

    @Test
    void encrypt_shouldCallStringEncryptorWithCorrectCardNumber() {
        // Given
        String cardNumber = "4111111111111111";
        String encryptedResult = "encrypted-result-456";

        when(encryptor.encrypt(cardNumber)).thenReturn(encryptedResult);

        // When
        String result = cardNumberEncryptor.encrypt(cardNumber);

        // Then
        verify(encryptor, times(1)).encrypt(cardNumber);
        assertEquals(encryptedResult, result);
    }
}