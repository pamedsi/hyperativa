package com.hyperativa.card.infra;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hyperativa.card.application.repository.BatchJPARepository;
import com.hyperativa.card.domain.Batch;
import com.hyperativa.handler.exceptions.APIException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BatchInfraRepositoryTest {

    @Mock
    private BatchJPARepository batchJPARepository;

    @InjectMocks
    private BatchInfraRepository batchInfraRepository;

    @Test
    void saveAndGet_shouldReturnSavedBatchWhenSaveIsSuccessful() {
        // Given
        Batch inputBatch = mock(Batch.class);
        Batch savedBatch = mock(Batch.class);

        when(batchJPARepository.saveAndFlush(inputBatch)).thenReturn(savedBatch);

        // When
        Batch result = batchInfraRepository.saveAndGet(inputBatch);

        // Then
        assertNotNull(result);
        assertEquals(savedBatch, result);
        verify(batchJPARepository, times(1)).saveAndFlush(inputBatch);
    }

    @Test
    void saveAndGet_shouldThrowAPIExceptionWhenRepositoryThrowsException() {
        // Given
        Batch batch = mock(Batch.class);

        when(batchJPARepository.saveAndFlush(batch))
                .thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(APIException.class, () ->
                batchInfraRepository.saveAndGet(batch)
        );
        verify(batchJPARepository, times(1)).saveAndFlush(batch);
    }

    @Test
    void save_shouldCallJPARepositorySaveWhenBatchIsValid() {
        // Given
        Batch batch = mock(Batch.class);

        // When
        batchInfraRepository.save(batch);

        // Then
        verify(batchJPARepository, times(1)).save(batch);
    }

    @Test
    void save_shouldThrowAPIExceptionWhenSaveFails() {
        // Given
        Batch batch = mock(Batch.class);

        doThrow(new RuntimeException("Save error"))
                .when(batchJPARepository).save(batch);

        // When & Then
        assertThrows(APIException.class, () ->
                batchInfraRepository.save(batch)
        );
        verify(batchJPARepository, times(1)).save(batch);
    }
}