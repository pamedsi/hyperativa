package com.hyperativa.auth.application.api;


import com.hyperativa.auth.application.service.TokenService;
import com.hyperativa.user.application.repository.UserRepository;
import com.hyperativa.user.domain.User;
import com.hyperativa.user.domain.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationRestControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationRestController authenticationRestController;

    @Test
    void login_shouldReturnAuthenticationResponseWithTokenAndRole() {
        // Given
        String login = "user@email.com";
        String password = "password123";
        String expectedToken = "generated.jwt.token";
        UserRole expectedRole = UserRole.CUSTOMER;

        UserCredentials credentials = new UserCredentials(login, password);
        User mockUser = createMockUser(expectedRole);
        Authentication mockAuthentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUser);
        when(tokenService.generateToken(mockUser)).thenReturn(expectedToken);

        // When
        AuthenticationResponse response = authenticationRestController.login(credentials);

        // Then
        assertNotNull(response);
        assertEquals(expectedToken, response.token());
        assertEquals(expectedRole, response.role());

        verify(authenticationManager).authenticate(argThat(token ->
                token.getPrincipal().equals(login) && token.getCredentials().equals(password)
        ));
        verify(tokenService).generateToken(mockUser);
        verify(mockAuthentication).getPrincipal();
    }

    @Test
    void login_shouldHandleAdminUserRole() {
        // Given
        String login = "admin@email.com";
        String password = "admin123";
        String expectedToken = "admin.jwt.token";
        UserRole expectedRole = UserRole.ADMIN;

        UserCredentials credentials = new UserCredentials(login, password);
        User mockUser = createMockUser(expectedRole);
        Authentication mockAuthentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUser);
        when(tokenService.generateToken(mockUser)).thenReturn(expectedToken);

        // When
        AuthenticationResponse response = authenticationRestController.login(credentials);

        // Then
        assertNotNull(response);
        assertEquals(expectedToken, response.token());
        assertEquals(expectedRole, response.role());
    }

    @Test
    void login_shouldCallAuthenticationManagerWithCorrectCredentials() {
        // Given
        String login = "test@email.com";
        String password = "test123";
        UserCredentials credentials = new UserCredentials(login, password);
        User mockUser = createMockUser(UserRole.CUSTOMER);
        Authentication mockAuthentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUser);
        when(tokenService.generateToken(mockUser)).thenReturn("token");

        // When
        authenticationRestController.login(credentials);

        // Then
        verify(authenticationManager).authenticate(argThat(authToken ->
                authToken instanceof UsernamePasswordAuthenticationToken &&
                        login.equals(authToken.getPrincipal()) &&
                        password.equals(authToken.getCredentials())
        ));
    }

    private User createMockUser(UserRole role) {
        User user = mock(User.class);
        when(user.getRole()).thenReturn(role);
        return user;
    }

    @Test
    void validateToken_shouldReturnValidTokenResponseWithUserDetails() {
        // Given
        String token = "Bearer valid.jwt.token";
        String email = "email@test.com";
        String expectedName = "John Doe";
        UserRole expectedRole = UserRole.CUSTOMER;
        User mockUser = mock(User.class);
        when(mockUser.getName()).thenReturn(expectedName);
        when(mockUser.getRole()).thenReturn(expectedRole);
        when(tokenService.decode(token)).thenReturn(email);
        when(userRepository.getUserByEmail(BDDMockito.eq(email))).thenReturn(mockUser);

        // When
        ValidTokenResponse response = authenticationRestController.validateToken(token);

        // Then
        assertNotNull(response);
        assertEquals(expectedName, response.name());
        assertEquals(expectedRole, response.role());
        verify(tokenService).decode(BDDMockito.eq(token));
        verify(userRepository).getUserByEmail(email);
    }

    @Test
    void validateToken_shouldHandleTokenWithoutBearerPrefix() {
        // Given
        String token = "raw.jwt.token";
        String email = "admin@email.com";
        String expectedName = "Admin User";
        UserRole expectedRole = UserRole.ADMIN;
        User mockUser = mock(User.class);
        when(mockUser.getName()).thenReturn(expectedName);
        when(mockUser.getRole()).thenReturn(expectedRole);

        when(tokenService.decode("raw.jwt.token")).thenReturn(email);
        when(userRepository.getUserByEmail(email)).thenReturn(mockUser);

        // When
        ValidTokenResponse response = authenticationRestController.validateToken(token);

        // Then
        assertNotNull(response);
        assertEquals(expectedName, response.name());
        assertEquals(expectedRole, response.role());
        verify(tokenService).decode("raw.jwt.token");
    }

    @Test
    void validateToken_shouldHandleAdminUserRole() {
        // Given
        String token = "admin.token";
        String email = "admin@company.com";
        String expectedName = "System Administrator";
        UserRole expectedRole = UserRole.ADMIN;

        User mockUser = mock(User.class);
        when(mockUser.getName()).thenReturn(expectedName);
        when(mockUser.getRole()).thenReturn(expectedRole);
        when(tokenService.decode(token)).thenReturn(email);
        when(userRepository.getUserByEmail(email)).thenReturn(mockUser);

        // When
        ValidTokenResponse response = authenticationRestController.validateToken(token);

        // Then
        assertEquals(expectedName, response.name());
        assertEquals(expectedRole, response.role());
    }

}
