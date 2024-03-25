package org.example.gymcrm.service.auth;

import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationCredentialsTest {

    @Mock
    private Logger log;

    @InjectMocks
    private AuthenticationCredentials authenticationCredentials;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetCredentialsWithUser_thenSuccess() {
        User user = new User();
        user.setUsername("Joe.Doe");
        user.setPassword("password123");

        Map<String, String> credentials = authenticationCredentials.getCredentials(new Object[]{user});

        assertNotNull(credentials, "Credentials map should not be null.");
        assertTrue(credentials.containsKey("username"), "Credentials map should contain 'username' key.");
        assertTrue(credentials.containsKey("password"), "Credentials map should contain 'password' key.");
        assertEquals("Joe.Doe", credentials.get("username"), "Username should match the expected value.");
        assertEquals("password123", credentials.get("password"), "Password should match the expected value.");
    }


    @Test
    void whenGetCredentialsWithStringArgs_thenSuccess() {
        String username = "janeDoe";
        String password = "password123";

        Map<String, String> credentials = authenticationCredentials.getCredentials(new Object[]{username, password});

        assertNotNull(credentials);
        assertEquals(username, credentials.get("username"));
        assertEquals(password, credentials.get("password"));
    }

    @Test
    void whenGetCredentialsWithTrainee_thenSuccess() {
        User user = new User();
        user.setUsername("traineeUser");
        user.setPassword("traineePass");
        Trainee trainee = new Trainee();
        trainee.setUser(user);

        Map<String, String> credentials = authenticationCredentials.getCredentials(new Object[]{trainee});

        assertNotNull(credentials);
        assertEquals("traineeUser", credentials.get("username"));
        assertEquals("traineePass", credentials.get("password"));
    }

    @Test
    void whenGetCredentialsWithTrainer_thenSuccess() {
        User user = new User();
        user.setUsername("trainerUser");
        user.setPassword("trainerPass");
        Trainer trainer = new Trainer();
        trainer.setUser(user);

        Map<String, String> credentials = authenticationCredentials.getCredentials(new Object[]{trainer});

        assertNotNull(credentials);
        assertEquals("trainerUser", credentials.get("username"));
        assertEquals("trainerPass", credentials.get("password"));
    }

    @Test
    void whenGetCredentialsWithNoArgs_thenThrowIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                authenticationCredentials.getCredentials(new Object[]{}));

        assertEquals("Authentication credentials not provided", exception.getMessage());
    }
}
