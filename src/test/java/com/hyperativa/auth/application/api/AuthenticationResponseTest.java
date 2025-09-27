package com.hyperativa.auth.application.api;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.hyperativa.user.domain.UserRole;

class AuthenticationResponseTest {

    @Test
    void authenticationResponse_shouldCreateWithCorrectValues() {
        // Given
        String expectedToken = "jwt.token.here";
        UserRole expectedRole = UserRole.ADMIN;

        // When
        AuthenticationResponse response = new AuthenticationResponse(expectedToken, expectedRole);

        // Then
        assertNotNull(response);
        assertEquals(expectedToken, response.token());
        assertEquals(expectedRole, response.role());
    }

    @Test
    void authenticationResponse_shouldHandleNullToken() {
        // Given
        UserRole expectedRole = UserRole.CUSTOMER;

        // When
        AuthenticationResponse response = new AuthenticationResponse(null, expectedRole);

        // Then
        assertNull(response.token());
        assertEquals(expectedRole, response.role());
    }

    @Test
    void authenticationResponse_shouldBeEqualWithSameValues() {
        // Given
        String token = "same.token";
        UserRole role = UserRole.CUSTOMER;

        // When
        AuthenticationResponse response1 = new AuthenticationResponse(token, role);
        AuthenticationResponse response2 = new AuthenticationResponse(token, role);

        // Then
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void authenticationResponse_shouldNotBeEqualWithDifferentValues() {
        // Given
        AuthenticationResponse response1 = new AuthenticationResponse("token1", UserRole.ADMIN);
        AuthenticationResponse response2 = new AuthenticationResponse("token2", UserRole.CUSTOMER);

        // Then
        assertNotEquals(response1, response2);
    }

    @Test
    void authenticationResponse_toStringShouldContainValues() {
        // Given
        String token = "test.token";
        UserRole role = UserRole.ADMIN;
        AuthenticationResponse response = new AuthenticationResponse(token, role);

        // When
        String toStringResult = response.toString();

        // Then
        assertTrue(toStringResult.contains(token));
        assertTrue(toStringResult.contains(role.name()));
    }
}
