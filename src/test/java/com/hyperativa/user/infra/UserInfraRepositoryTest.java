package com.hyperativa.user.infra;

import com.hyperativa.handler.exceptions.APIException;
import com.hyperativa.handler.exceptions.UserNotFoundException;
import com.hyperativa.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserInfraRepositoryTest {

    @Mock
    private UserJPARepository userJPARepository;

    @InjectMocks
    private UserInfraRepository userInfraRepository;

    @Test
    void getUserByEmail_shouldReturnUserWhenEmailExistsAndNotDeleted() {
        // Given
        String email = "user@email.com";
        User expectedUser = mock(User.class);

        when(userJPARepository.findByEmailAndDeletedFalse(email))
                .thenReturn(Optional.of(expectedUser));

        // When
        User result = userInfraRepository.getUserByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(expectedUser, result);
        verify(userJPARepository, times(1)).findByEmailAndDeletedFalse(email);
    }

    @Test
    void getUserByEmail_shouldThrowUserNotFoundExceptionWhenEmailNotFound() {
        // Given
        String nonExistentEmail = "nonexistent@email.com";

        when(userJPARepository.findByEmailAndDeletedFalse(nonExistentEmail))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () ->
                userInfraRepository.getUserByEmail(nonExistentEmail)
        );
        verify(userJPARepository, times(1)).findByEmailAndDeletedFalse(nonExistentEmail);
    }

    @Test
    void getUserByIdentifier_shouldReturnUserWhenIdentifierExistsAndNotDeleted() {
        // Given
        UUID userIdentifier = UUID.randomUUID();
        User expectedUser = mock(User.class);

        when(userJPARepository.findByIdentifierAndDeletedFalse(userIdentifier))
                .thenReturn(Optional.of(expectedUser));

        // When
        User result = userInfraRepository.getUserByIdentifier(userIdentifier);

        // Then
        assertNotNull(result);
        assertEquals(expectedUser, result);
        verify(userJPARepository, times(1)).findByIdentifierAndDeletedFalse(userIdentifier);
    }

    @Test
    void getUserByIdentifier_shouldThrowAPIExceptionWhenIdentifierNotFound() {
        // Given
        UUID nonExistentIdentifier = UUID.randomUUID();

        when(userJPARepository.findByIdentifierAndDeletedFalse(nonExistentIdentifier))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(APIException.class, () ->
                userInfraRepository.getUserByIdentifier(nonExistentIdentifier)
        );
        verify(userJPARepository, times(1)).findByIdentifierAndDeletedFalse(nonExistentIdentifier);
    }

    @Test
    void existsByEmail_shouldReturnTrue_whenEmailExistsAndNotDeleted() {
        // Given
        String email = "test@example.com";
        when(userJPARepository.existsByEmailAndDeletedFalse(email)).thenReturn(true);

        // When
        boolean result = userInfraRepository.existsByEmail(email);

        // Then
        assertTrue(result);
        verify(userJPARepository, times(1)).existsByEmailAndDeletedFalse(email);
    }

    @Test
    void existsByEmail_shouldReturnFalse_whenEmailDoesNotExistOrIsDeleted() {
        // Given
        String email = "nonexistent@example.com";
        when(userJPARepository.existsByEmailAndDeletedFalse(email)).thenReturn(false);

        // When
        boolean result = userInfraRepository.existsByEmail(email);

        // Then
        assertFalse(result);
        verify(userJPARepository, times(1)).existsByEmailAndDeletedFalse(email);
    }


}