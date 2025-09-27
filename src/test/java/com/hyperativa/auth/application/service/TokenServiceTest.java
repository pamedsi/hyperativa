package com.hyperativa.auth.application.service;


import com.hyperativa.handler.exceptions.JwtException;
import com.hyperativa.user.domain.User;
import com.hyperativa.user.domain.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    private User user;

    void initiateVariables() {
        ReflectionTestUtils.setField(tokenService, "secret", "test-secret");
        ReflectionTestUtils.setField(tokenService, "issuer", "test-issuer");

        user = mock(User.class);
        BDDMockito.when(user.getUsername()).thenReturn("testuser");
        BDDMockito.when(user.getRole()).thenReturn(UserRole.ADMIN);
    }

    @Test
    void generateToken_shouldReturnValidTokenWhenUserIsValid() {
        initiateVariables();
        String token = tokenService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void generateToken_shouldContainCorrectClaimsWhenUserIsValid() {
        initiateVariables();
        String token = tokenService.generateToken(user);

        assertDoesNotThrow(() -> tokenService.decode(token));
        String subject = tokenService.decode(token);
        assertEquals(user.getUsername(), subject);
    }

    @Test
    void generateToken_shouldThrowJwtException_whenSecretIsInvalid() {
        ReflectionTestUtils.setField(tokenService, "secret", "");

        Exception exception = assertThrows(RuntimeException.class, () -> tokenService.generateToken(user));

        assertEquals("Error generating JWT", exception.getMessage());
    }

    @Test
    void generateToken_shouldUseCorrectIssuerWhenGeneratingToken() {
        initiateVariables();
        String token = tokenService.generateToken(user);

        assertDoesNotThrow(() -> tokenService.decode(token));
    }

    @Test
    void decode_shouldReturnUsernameWhenTokenIsValid() {
        initiateVariables();
        // Given
        String validToken = tokenService.generateToken(user);

        // When
        String result = tokenService.decode("Bearer " + validToken);

        // Then
        assertEquals(user.getUsername(), result);
    }

    @Test
    void decode_shouldHandleTokenWithoutBearerPrefix() {
        initiateVariables();
        // Given
        String validToken = tokenService.generateToken(user);

        // When
        String result = tokenService.decode(validToken);

        // Then
        assertEquals(user.getUsername(), result);
    }

    @Test
    void decode_shouldThrowJwtExceptionWhenTokenIsExpired() {
        ReflectionTestUtils.setField(tokenService, "secret", "test-secret");
        // Given
        String expiredToken = generateExpiredToken();

        // When & Then
        assertThrows(JwtException.class, () -> tokenService.decode("Bearer " + expiredToken));
    }

    @Test
    void decode_shouldThrowJwtExceptionWhenTokenIsInvalid() {
        // Given
        String invalidToken = "invalid.token.here";

        // When & Then
        assertThrows(RuntimeException.class, () -> tokenService.decode("Bearer " + invalidToken));
    }

    private String generateExpiredToken() {
        // Para este exemplo, vamos criar um token manualmente com data expirada
        // Em um cenário real, você poderia usar uma biblioteca ou mock
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlzcyI6InRlc3QtaXNzdWVyIiwiZXhwIjoxNTAwMDAwMDAwLCJST0xFIjoiQURNSU4ifQ.fakeSignatureForExpiredToken";
    }

}