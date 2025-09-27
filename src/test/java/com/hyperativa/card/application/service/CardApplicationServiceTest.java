package com.hyperativa.card.application.service;


import com.hyperativa.card.application.api.CardId;
import com.hyperativa.card.domain.Card;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hyperativa.card.application.api.CardNumber;
import com.hyperativa.user.domain.User;
import com.hyperativa.user.application.repository.UserRepository;
import com.hyperativa.card.application.repository.CardRepository;
import com.hyperativa.utils.Hasher;
import com.hyperativa.card.infra.CardNumberEncryptor;
import com.hyperativa.card.application.repository.BatchRepository;
import com.hyperativa.card.domain.Batch;
import com.hyperativa.handler.exceptions.InvalidFileException;

import jakarta.validation.Validator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardApplicationServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BatchRepository batchRepository;

    @Mock
    private Validator validator;

    @Mock
    private Hasher hasher;

    @Mock
    private CardNumberEncryptor encryptor;

    @InjectMocks
    private CardApplicationService cardApplicationService;

    @Test
    void addSingleCard_shouldSaveCardWithHashedAndEncryptedNumberWhenValidInput() {
        // Given
        CardNumber cardNumber = new CardNumber("1234567890123456");
        String ownerEmail = "user@email.com";
        User mockUser = mock(User.class);
        String hashedNumber = "hashed-card-number";
        String encryptedNumber = "encrypted-card-number";

        when(userRepository.getUserByEmail(ownerEmail)).thenReturn(mockUser);
        when(hasher.hashCardNumber(cardNumber.cardNumber())).thenReturn(hashedNumber);
        when(encryptor.encrypt(cardNumber.cardNumber())).thenReturn(encryptedNumber);

        // When
        cardApplicationService.addSingleCard(cardNumber, ownerEmail);

        // Then
        verify(userRepository, times(1)).getUserByEmail(ownerEmail);
        verify(hasher, times(1)).hashCardNumber(cardNumber.cardNumber());
        verify(encryptor, times(1)).encrypt(cardNumber.cardNumber());
        verify(cardRepository, times(1)).save(argThat(card ->
                card.getOwner() == mockUser &&
                        card.getCardNumberHash().equals(hashedNumber) &&
                        card.getEncryptedCardNumber().equals(encryptedNumber)
        ));
    }

    @Test
    void addSingleCard_shouldUseCorrectCardNumberWhenProcessingCard() {
        // Given
        String expectedCardNumber = "4111111111111111";
        CardNumber cardNumber = new CardNumber(expectedCardNumber);
        String ownerEmail = "owner@domain.com";
        User mockUser = mock(User.class);

        when(userRepository.getUserByEmail(ownerEmail)).thenReturn(mockUser);
        when(hasher.hashCardNumber(expectedCardNumber)).thenReturn("hashed-123");
        when(encryptor.encrypt(expectedCardNumber)).thenReturn("encrypted-123");

        // When
        cardApplicationService.addSingleCard(cardNumber, ownerEmail);

        // Then
        verify(hasher, times(1)).hashCardNumber(expectedCardNumber);
        verify(encryptor, times(1)).encrypt(expectedCardNumber);
        verify(cardRepository, times(1)).save(any());
    }
    @Test
    void addBatchCards_shouldProcessBatchSuccessfullyWhenValidFileAndData() throws IOException {
        // Given
        MultipartFile file = new MockMultipartFile(
                "input-test.txt",
                "input-test.txt",
                "text/plain",
                new ClassPathResource("input-test.txt").getInputStream()
        );
        String ownerEmail = "owner@email.com";
        User owner = mock(User.class);
        when(userRepository.getUserByEmail(ownerEmail)).thenReturn(owner);

        Batch mockBatch = mock(Batch.class);
        when(batchRepository.saveAndGet(any(Batch.class))).thenReturn(mockBatch);

        // When
        cardApplicationService.addBatchCards(file, ownerEmail);

        // Then
        verify(userRepository, times(1)).getUserByEmail(ownerEmail);
        verify(batchRepository, times(1)).saveAndGet(any(Batch.class));
        verify(cardRepository, times(1)).saveAll(anyList());
        verify(batchRepository, times(1)).save(mockBatch);
    }

    @Test
    void addBatchCards_shouldThrowInvalidFileExceptionWhenFileHasLessThanThreeLines() {
        // Given
        // Criar um arquivo temporário com apenas 2 linhas
        String invalidContent = "HEADER_LINE\nLINE2";
        MultipartFile file = new MockMultipartFile(
                "invalid.txt",
                "invalid.txt",
                "text/plain",
                invalidContent.getBytes(StandardCharsets.UTF_8)
        );
        String ownerEmail = "owner@email.com";
        User owner = mock(User.class);

        when(userRepository.getUserByEmail(ownerEmail)).thenReturn(owner);

        // When & Then
        assertThrows(InvalidFileException.class, () ->
                cardApplicationService.addBatchCards(file, ownerEmail)
        );

        verify(batchRepository, never()).saveAndGet(any(Batch.class));
        verify(cardRepository, never()).saveAll(anyList());
    }

    @Test
    void getCardInfo_shouldReturnCardIdWhenCardExistsForOwner() {
        // Given
        String cardNumber = "1234567890123456";
        String ownerEmail = "owner@email.com";
        User owner = mock(User.class);
        String hashedNumber = "hashed-card-number";
        Card mockCard = mock(Card.class);
        UUID expectedCardId = UUID.randomUUID();

        when(userRepository.getUserByEmail(ownerEmail)).thenReturn(owner);
        when(hasher.hashCardNumber(cardNumber)).thenReturn(hashedNumber);
        when(cardRepository.getCardByOwner(owner, hashedNumber)).thenReturn(mockCard);
        when(mockCard.getIdentifier()).thenReturn(expectedCardId);

        // When
        CardId result = cardApplicationService.getCardInfo(cardNumber, ownerEmail);

        // Then
        assertNotNull(result);
        assertEquals(expectedCardId, result.cardIdentifier());
        verify(userRepository, times(1)).getUserByEmail(ownerEmail);
        verify(hasher, times(1)).hashCardNumber(cardNumber);
        verify(cardRepository, times(1)).getCardByOwner(owner, hashedNumber);
    }

    @Test
    void getCardInfo_shouldUseCorrectHashedNumberWhenSearchingCard() {
        // Given
        String cardNumber = "4111111111111111";
        String ownerEmail = "user@domain.com";
        User owner = mock(User.class);
        String expectedHash = "specific-hash-value";
        Card mockCard = mock(Card.class);
        UUID cardId = UUID.randomUUID();

        when(userRepository.getUserByEmail(ownerEmail)).thenReturn(owner);
        when(hasher.hashCardNumber(cardNumber)).thenReturn(expectedHash);
        when(cardRepository.getCardByOwner(owner, expectedHash)).thenReturn(mockCard);
        when(mockCard.getIdentifier()).thenReturn(cardId);

        // When
        CardId result = cardApplicationService.getCardInfo(cardNumber, ownerEmail);

        // Then
        assertEquals(cardId, result.cardIdentifier());
        verify(hasher, times(1)).hashCardNumber(cardNumber);
        verify(cardRepository, times(1)).getCardByOwner(owner, expectedHash);
    }

}