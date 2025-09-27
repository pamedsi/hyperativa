package com.hyperativa.card.domain;

import org.junit.jupiter.api.Test;
import com.hyperativa.card.application.service.BatchHeader;
import com.hyperativa.user.domain.User;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BatchTest {

    @Test
    void batch_shouldCreateInstanceWithCorrectValuesWhenBatchHeaderAndUserProvided() {
        // Given
        BatchHeader batchHeader = new BatchHeader("Test Name", "20231201", "BATCH001", 100, "testfile.txt");
        User processedBy = mock(User.class);

        // When
        Batch batch = new Batch(batchHeader, processedBy);

        // Then
        assertEquals("testfile.txt", batch.getFilename());
        assertEquals(100, batch.getTotalRecords());
        assertEquals(processedBy, batch.getProcessedBy());
        assertEquals(BatchStatus.PROCESSING, batch.getStatus());
        assertEquals("Test Name", batch.getName());
        assertEquals("20231201", batch.getDate());
        assertEquals("BATCH001", batch.getBatchNumber());
        assertEquals(0, batch.getSuccessfulRecords());
        assertEquals(0, batch.getFailedRecords());
        assertNotNull(batch.getProcessedAt());
    }

    @Test
    void markAsCompleted_shouldUpdateStatusAndTimestampWhenCalled() {
        // Given
        BatchHeader batchHeader = new BatchHeader("Test", "20231201", "BATCH001", 10, "file.txt");
        User user = mock(User.class);
        Batch batch = new Batch(batchHeader, user);
        LocalDateTime initialProcessedAt = batch.getProcessedAt();

        // When
        batch.markAsCompleted();

        // Then
        assertEquals(BatchStatus.COMPLETED, batch.getStatus());
        assertNotEquals(initialProcessedAt, batch.getProcessedAt());
        assertTrue(batch.getProcessedAt().isAfter(initialProcessedAt));
    }

    @Test
    void markAsFailed_shouldUpdateStatusToFailedWhenCalled() {
        // Given
        BatchHeader batchHeader = new BatchHeader("Test", "20231201", "BATCH001", 10, "file.txt");
        User user = mock(User.class);
        Batch batch = new Batch(batchHeader, user);
        LocalDateTime initialProcessedAt = batch.getProcessedAt();

        // When
        batch.markAsFailed();

        // Then
        assertEquals(BatchStatus.FAILED, batch.getStatus());
        assertNotEquals(initialProcessedAt, batch.getProcessedAt());
        assertTrue(batch.getProcessedAt().isAfter(initialProcessedAt));
    }

    @Test
    void markAsPartialSuccess_shouldUpdateStatusAndCountsWhenCalled() {
        // Given
        BatchHeader batchHeader = new BatchHeader("Test", "20231201", "BATCH001", 10, "file.txt");
        User user = mock(User.class);
        Batch batch = new Batch(batchHeader, user);
        LocalDateTime initialProcessedAt = batch.getProcessedAt();
        int successful = 7;
        int failed = 3;

        // When
        batch.markAsPartialSuccess(successful, failed);

        // Then
        assertEquals(BatchStatus.PARTIAL_SUCCESS, batch.getStatus());
        assertEquals(successful, batch.getSuccessfulRecords());
        assertEquals(failed, batch.getFailedRecords());
        assertNotEquals(initialProcessedAt, batch.getProcessedAt());
        assertTrue(batch.getProcessedAt().isAfter(initialProcessedAt));
    }
}