package com.hyperativa.handler;

import com.hyperativa.handler.exceptions.APIException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestExceptionHandlerTest {

    @InjectMocks
    private RestExceptionHandler exceptionHandler;

    @Mock
    private HttpServletRequest request;

    @Test
    void handleValidationExceptionsTest() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        WebExchangeBindException bindingResult = mock(WebExchangeBindException.class);
        ObjectError error = new ObjectError("object", "Validation error message");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(error));
        when(request.getServletPath()).thenReturn("/api/test");
        when(request.getMethod()).thenReturn("POST");

        // When
        ResponseEntity<ExceptionDetails> response = exceptionHandler.handleValidationExceptions(exception, request);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation error message", response.getBody().message());
        assertEquals(400, response.getBody().status());
    }

    @Test
    void handleAuthenticationExceptionsTest() {
        // Given
        InternalAuthenticationServiceException exception = new InternalAuthenticationServiceException("Auth failed");
        when(request.getServletPath()).thenReturn("/api/login");
        when(request.getMethod()).thenReturn("POST");

        // When
        ResponseEntity<ExceptionDetails> response = exceptionHandler.handleAuthenticationExceptions(exception, request);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Email or password incorrect!", response.getBody().message());
        assertEquals(401, response.getBody().status());
    }

    @Test
    void apiExceptionHandlerTest() {
        // Given
        APIException exception = new APIException("Custom API error", HttpStatus.BAD_REQUEST);
        when(request.getServletPath()).thenReturn("/api/users");
        when(request.getMethod()).thenReturn("GET");

        // When
        ResponseEntity<ExceptionDetails> response = exceptionHandler.apiExceptionHandler(exception, request);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Custom API error", response.getBody().message());
        assertEquals(400, response.getBody().status());
    }

    @Test
    void generalExceptionTest() {
        // Given
        Exception exception = new Exception("General error");
        when(request.getServletPath()).thenReturn("/api/endpoint");
        when(request.getMethod()).thenReturn("PUT");

        // When
        ResponseEntity<ExceptionDetails> response = exceptionHandler.generalException(exception, request);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Internal error! Please contact support or try again later.", response.getBody().message());
        assertEquals(500, response.getBody().status());
    }

}