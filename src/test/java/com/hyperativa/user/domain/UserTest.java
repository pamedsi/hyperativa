package com.hyperativa.user.domain;

import org.junit.jupiter.api.Test;
import com.hyperativa.user.application.api.CreateUserRequest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserTest {

    @Test
    void user_shouldCreateInstanceWithCorrectValuesWhenValidCreateUserRequest() {
        // Given
        CreateUserRequest request = mock(CreateUserRequest.class);
        when(request.name()).thenReturn("John Doe");
        when(request.email()).thenReturn("john@email.com");
        when(request.getBirthdate()).thenReturn(LocalDate.of(1990, 1, 1));
        String hashedPassword = "hashed-password-123";

        // When
        User user = new User(request, hashedPassword);

        // Then
        assertNotNull(user.getIdentifier());
        assertEquals("John Doe", user.getName());
        assertEquals("john@email.com", user.getEmail());
        assertEquals(hashedPassword, user.getPassword());
        assertEquals(LocalDate.of(1990, 1, 1), user.getBirthdate());
        assertEquals(UserRole.CUSTOMER, user.getRole());
        assertFalse(user.isAdmin());
    }

    @Test
    void delete_shouldMarkAccountAsDeletedWhenCalled() {
        // Given
        CreateUserRequest request = mock(CreateUserRequest.class);
        User user = new User(request, "password");

        // When
        user.delete();

        // Then
        assertTrue(user.getDeleted());
    }

}