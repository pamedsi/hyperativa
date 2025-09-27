package com.hyperativa.user.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hyperativa.user.application.api.CreateUserRequest;
import com.hyperativa.user.domain.User;
import com.hyperativa.user.application.repository.UserRepository;
import com.hyperativa.utils.Hasher;

import java.util.Objects;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserApplicationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Hasher hasher;

    @InjectMocks
    private UserApplicationService userApplicationService;

    @Test
    void createUser_shouldSaveUserWithHashedPasswordWhenValidRequest() {
        // Given
        CreateUserRequest request = mock(CreateUserRequest.class);
        String rawPassword = "plain-password";
        String hashedPassword = "hashed-password-123";

        when(request.password()).thenReturn(rawPassword);
        when(hasher.hashPassword(rawPassword)).thenReturn(hashedPassword);

        // When
        userApplicationService.createUser(request);

        // Then
        verify(hasher, times(1)).hashPassword(rawPassword);
        verify(userRepository, times(1)).saveUser(argThat(user ->
                user != null && user.getPassword().equals(hashedPassword)
        ));
    }

    @Test
    void createUser_shouldUseCreateUserRequestDataWhenBuildingUser() {
        // Given
        CreateUserRequest request = mock(CreateUserRequest.class);
        String password = "test-password";
        String hashedPassword = "hashed-test-password";

        when(request.password()).thenReturn(password);
        when(hasher.hashPassword(password)).thenReturn(hashedPassword);

        // When
        userApplicationService.createUser(request);

        // Then
        verify(userRepository, times(1)).saveUser(argThat(Objects::nonNull));
        verify(hasher, times(1)).hashPassword(password);
    }

    @Test
    void deleteUser_shouldDeleteUserWhenUserIsAdmin() {
        // Given
        String email = "admin@email.com";
        UUID userIdentifier = UUID.randomUUID();
        User adminUser = mock(User.class);
        User userToDelete = mock(User.class);

        when(userRepository.getUserByEmail(email)).thenReturn(adminUser);
        when(userRepository.getUserByIdentifier(userIdentifier)).thenReturn(userToDelete);
        when(adminUser.isAdmin()).thenReturn(true);

        // When
        userApplicationService.deleteUser(email, userIdentifier);

        // Then
        verify(userToDelete, times(1)).delete();
        verify(userRepository, times(1)).saveUser(userToDelete);
    }

    @Test
    void deleteUser_shouldDeleteUserWhenUserIsDeletingThemselves() {
        // Given
        String email = "user@email.com";
        UUID userIdentifier = UUID.randomUUID();
        User sameUser = mock(User.class);

        when(userRepository.getUserByEmail(email)).thenReturn(sameUser);
        when(userRepository.getUserByIdentifier(userIdentifier)).thenReturn(sameUser);
        when(sameUser.isAdmin()).thenReturn(false);

        // When
        userApplicationService.deleteUser(email, userIdentifier);

        // Then
        verify(sameUser, times(1)).delete();
        verify(userRepository, times(1)).saveUser(sameUser);
    }

}