package com.hyperativa.auth.application.service;


import com.hyperativa.user.application.repository.UserRepository;
import com.hyperativa.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void loadUserByUsername_shouldReturnUserDetailsWhenUserExists() {
        // Given
        String email = "user@email.com";
        User mockUser = mock(User.class);

        when(userRepository.getUserByEmail(email)).thenReturn(mockUser);

        // When
        UserDetails result = authenticationService.loadUserByUsername(email);

        // Then
        assertNotNull(result);
        assertEquals(mockUser, result);
        verify(userRepository).getUserByEmail(email);
    }

    @Test
    void loadUserByUsername_shouldCallRepositoryWithCorrectEmail() {
        // Given
        String email = "test@example.com";
        User mockUser = mock(User.class);

        when(userRepository.getUserByEmail(email)).thenReturn(mockUser);

        // When
        authenticationService.loadUserByUsername(email);

        // Then
        verify(userRepository).getUserByEmail(email);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void loadUserByUsername_shouldHandleCaseSensitiveEmail() {
        // Given
        String mixedCaseEmail = "User@Email.COM";
        User mockUser = mock(User.class);

        when(userRepository.getUserByEmail(mixedCaseEmail)).thenReturn(mockUser);

        // When
        UserDetails result = authenticationService.loadUserByUsername(mixedCaseEmail);

        // Then
        assertNotNull(result);
        verify(userRepository).getUserByEmail(mixedCaseEmail);
    }

}
