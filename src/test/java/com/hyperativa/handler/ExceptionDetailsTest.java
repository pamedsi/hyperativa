package com.hyperativa.handler;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionDetailsTest {

    @Test
    void exceptionDetails_shouldCreateInstanceWithAllFields_whenValidDataProvided() {
        // Given
        String title = "Bad Request";
        String message = "Invalid input data";
        int status = 400;
        String timestamp = "2023-10-01T10:00:00";
        String path = "/api/users";
        String method = "POST";

        // When
        ExceptionDetails details = new ExceptionDetails(title, message, status, timestamp, path, method);

        // Then
        assertEquals(title, details.title());
        assertEquals(message, details.message());
        assertEquals(status, details.status());
        assertEquals(timestamp, details.timestamp());
        assertEquals(path, details.path());
        assertEquals(method, details.method());
    }

    @Test
    void exceptionDetails_shouldCreateInstanceWithDifferentValues_whenAlternativeDataProvided() {
        // Given
        String title = "Not Found";
        String message = "Resource not found";
        int status = 404;
        String timestamp = "2023-10-01T11:30:00";
        String path = "/api/products/123";
        String method = "GET";

        // When
        ExceptionDetails details = new ExceptionDetails(title, message, status, timestamp, path, method);

        // Then
        assertEquals(title, details.title());
        assertEquals(message, details.message());
        assertEquals(status, details.status());
        assertEquals(timestamp, details.timestamp());
        assertEquals(path, details.path());
        assertEquals(method, details.method());
    }
}