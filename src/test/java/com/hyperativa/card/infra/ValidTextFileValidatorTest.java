package com.hyperativa.card.infra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidatorContext;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ValidTextFileValidatorTest {

    private ValidTextFileValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new ValidTextFileValidator();
        context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

        // Mock the constraint annotation configuration
        ValidTextFile constraintAnnotation = mock(ValidTextFile.class);
        when(constraintAnnotation.maxSize()).thenReturn(1024L * 1024L); // 1MB
        when(constraintAnnotation.allowedTypes()).thenReturn(new String[]{"text/plain", "application/json"});

        validator.initialize(constraintAnnotation);

        // Setup mock chain for constraint violation
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addConstraintViolation()).thenReturn(context);
    }

    @Test
    void isValid_shouldReturnFalse_whenFileIsNull() {
        // Given
        MultipartFile nullFile = null;

        // When
        boolean result = validator.isValid(nullFile, context);

        // Then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Text file cannot be null or empty");
    }

    @Test
    void isValid_shouldReturnFalse_whenFileIsEmpty() {
        // Given
        MultipartFile emptyFile = new MockMultipartFile(
                "empty.txt",
                "empty.txt",
                "text/plain",
                new byte[0]
        );

        // When
        boolean result = validator.isValid(emptyFile, context);

        // Then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Text file cannot be null or empty");
    }

    @Test
    void isValid_shouldReturnFalse_whenFileExceedsMaxSize() {
        // Given
        byte[] largeContent = new byte[2 * 1024 * 1024]; // 2MB - larger than 1MB limit
        MultipartFile largeFile = new MockMultipartFile(
                "large.txt",
                "large.txt",
                "text/plain",
                largeContent
        );

        // When
        boolean result = validator.isValid(largeFile, context);

        // Then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("File too big, maximum size is 1048576");
    }

    @Test
    void isValid_shouldReturnFalse_whenContentTypeIsNotAllowed() {
        // Given
        MultipartFile invalidTypeFile = new MockMultipartFile(
                "image.jpg",
                "image.jpg",
                "image/jpeg",
                "content".getBytes()
        );

        // When
        boolean result = validator.isValid(invalidTypeFile, context);

        // Then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate(
                "File type not allowed, types allowed are: [text/plain, application/json]"
        );
    }

    @Test
    void isValid_shouldReturnFalse_whenFileIsNotReadableText() throws IOException {
        // Given
        MultipartFile unreadableFile = mock(MultipartFile.class);
        when(unreadableFile.isEmpty()).thenReturn(false);
        when(unreadableFile.getSize()).thenReturn(100L);
        when(unreadableFile.getContentType()).thenReturn("text/plain");
        when(unreadableFile.getBytes()).thenThrow(new IOException("Read error"));

        // When
        boolean result = validator.isValid(unreadableFile, context);

        // Then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("File is not a valid readable text");
    }

    @Test
    void isValid_shouldReturnFalse_whenFileContentIsEmptyAfterTrimming() {
        // Given
        MultipartFile emptyContentFile = new MockMultipartFile(
                "whitespace.txt",
                "whitespace.txt",
                "text/plain",
                "   \n\t   ".getBytes()
        );

        // When
        boolean result = validator.isValid(emptyContentFile, context);

        // Then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("File is not a valid readable text");
    }

    @Test
    void isValid_shouldReturnTrue_whenFileIsValidTextFile() {
        // Given
        MultipartFile validFile = new MockMultipartFile(
                "valid.txt",
                "valid.txt",
                "text/plain",
                "Valid text content".getBytes()
        );

        // When
        boolean result = validator.isValid(validFile, context);

        // Then
        assertTrue(result);
        verifyNoInteractions(context); // No constraint violations should be built
    }

    @Test
    void isValidContentType_shouldReturnTrue_whenContentTypeMatchesExactly() {
        // Given
        String contentType = "text/plain";

        // When
        boolean result = validator.isValidContentType(contentType);

        // Then
        assertTrue(result);
    }

    @Test
    void isValidContentType_shouldReturnFalse_whenContentTypeIsNull() {
        // Given
        String contentType = null;

        // When
        boolean result = validator.isValidContentType(contentType);

        // Then
        assertFalse(result);
    }

    @Test
    void isValidContentType_shouldReturnFalse_whenContentTypeIsNotAllowed() {
        // Given
        String contentType = "image/jpeg";

        // When
        boolean result = validator.isValidContentType(contentType);

        // Then
        assertFalse(result);
    }
}