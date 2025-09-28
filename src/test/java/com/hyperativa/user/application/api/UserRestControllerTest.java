package com.hyperativa.user.application.api;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hyperativa.auth.application.service.TokenService;
import com.hyperativa.user.application.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserRestController userRestController;

    @Test
    void createUser_shouldCallUserServiceCreateUser_whenValidRequestProvided() {
        // Given
        CreateUserRequest validRequest = new CreateUserRequest(
                "John Doe",
                "john.doe@example.com",
                "SecurePassword123!",
                "01/01/1991"
//                java.time.LocalDate.of(1990, 1, 1)
        );

        // When
        userRestController.createUser(validRequest);

        // Then
        verify(userService, times(1)).createUser(validRequest);
        verifyNoInteractions(tokenService);
    }

    @Test
    void createUser_shouldCallUserServiceCreateUser_whenRequestWithMinimumValidData() {
        // Given
        CreateUserRequest minimalValidRequest = new CreateUserRequest(
                "Jane Smith",
                "jane.smith@example.com",
                "AnotherPass123!",
                "12/25/1990"
        );

        // When
        userRestController.createUser(minimalValidRequest);

        // Then
        verify(userService, times(1)).createUser(minimalValidRequest);
        verifyNoInteractions(tokenService);
    }

    @Test
    void createUser_shouldCallUserServiceCreateUser_whenRequestWithBoundaryValues() {
        // Given
        LocalDate date = LocalDate.now().minusYears(18);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        CreateUserRequest boundaryRequest = new CreateUserRequest(
                "A",
                "a@b.co",
                "Pass1!",
                date.format(formatter) // Exactly 18 years old
        );

        // When
        userRestController.createUser(boundaryRequest);

        // Then
        verify(userService, times(1)).createUser(boundaryRequest);
        verifyNoInteractions(tokenService);
    }

    @Test
    void deleteUser_shouldCallServiceMethods_whenValidTokenAndIdentifierProvided() {
        // Given
        String validToken = "jwt.token.here";
        UUID userIdentifier = UUID.randomUUID();
        String decodedEmail = "user@example.com";

        when(tokenService.decode(validToken)).thenReturn(decodedEmail);

        // When
        userRestController.deleteUser(validToken, userIdentifier);

        // Then
        verify(tokenService, times(1)).decode(validToken);
        verify(userService, times(1)).deleteUser(decodedEmail, userIdentifier);
    }

    @Test
    void deleteUser_shouldCallServiceMethods_whenDifferentValidInputs() {
        // Given
        String validToken = "another.jwt.token";
        UUID userIdentifier = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        String decodedEmail = "another.user@test.com";

        when(tokenService.decode(validToken)).thenReturn(decodedEmail);

        // When
        userRestController.deleteUser(validToken, userIdentifier);

        // Then
        verify(tokenService, times(1)).decode(validToken);
        verify(userService, times(1)).deleteUser(decodedEmail, userIdentifier);
    }

}