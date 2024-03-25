package org.example.gymcrm.service.auth;

import org.example.gymcrm.model.User;
import org.example.gymcrm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticate_WithValidCredentials_ShouldReturnTrue() {
        // Arrange
        String username = "testUser";
        String password = "testPass";
        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(password);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // Act
        boolean result = authenticationService.authenticate(username, password);

        // Assert
        assertTrue(result, "Expected authentication to succeed with correct credentials.");
    }

    @Test
    void authenticate_WithInvalidPassword_ShouldReturnFalse() {
        // Arrange
        String username = "testUser";
        String correctPassword = "correctPass";
        String wrongPassword = "wrongPass";
        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(correctPassword);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // Act
        boolean result = authenticationService.authenticate(username, wrongPassword);

        // Assert
        assertFalse(result, "Expected authentication to fail with incorrect password.");
    }

    @Test
    void authenticate_WithNonExistentUser_ShouldThrowException() {
        // Arrange
        String username = "nonExistentUser";
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> authenticationService.authenticate(username, "anyPassword"),
                "Expected exception to be thrown for non-existent user.");
    }
}
