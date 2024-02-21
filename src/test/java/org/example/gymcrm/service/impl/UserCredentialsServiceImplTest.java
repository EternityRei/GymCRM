package org.example.gymcrm.service.impl;

import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dao.UserDao;
import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserCredentialsServiceImplTest {
    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserCredentialsServiceImpl userCredentialsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    /**
     * Tests to create credentials for Trainee
     */

    @Test
    void testCreateTraineeWithUniqueUsername() {
        // Given
        Trainee newTrainee = new Trainee();
        newTrainee.setFirstName("John");
        newTrainee.setLastName("Doe");

        when(userDao.findUsers(any(Trainee.class)))
                .thenReturn(Collections.emptyList());
        when(userDao.save(any(Trainee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        User createdTrainee = userCredentialsService.createCredentials(newTrainee);

        // Then
        verify(userDao, times(1)).save(any(Trainee.class));
        assertNotNull(createdTrainee.getUsername(), "Username should not be null");
        assertTrue(createdTrainee.getUsername().startsWith("John.Doe"), "Username should start with 'John.Doe'");
        assertEquals(10, createdTrainee.getPassword().length(), "Password should be 10 characters long");
    }

    @Test
    void testCreateTraineeWithDuplicateUsername() {
        // Given
        Trainee existingTrainee = new Trainee();
        existingTrainee.setUsername("John.Doe");

        Trainee newTrainee = new Trainee();
        newTrainee.setFirstName("John");
        newTrainee.setLastName("Doe");

        when(userDao.findUsers(any(Trainee.class)))
                .thenReturn(List.of(existingTrainee));
        when(userDao.save(any(Trainee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        User createdTrainee = userCredentialsService.createCredentials(newTrainee);

        // Then
        verify(userDao, times(1)).save(any(Trainee.class));
        assertNotEquals("John.Doe", createdTrainee.getUsername(), "Username should not be 'John.Doe' to avoid duplication");
        assertEquals(10, createdTrainee.getPassword().length(), "Password should be 10 characters long");
    }

    @Test
    void testCreateSequentialTrainees() {
        // Setup
        Trainee firstTrainee = new Trainee();
        firstTrainee.setFirstName("Joe");
        firstTrainee.setLastName("Doe");

        Trainee secondTrainee = new Trainee();
        secondTrainee.setFirstName("Joe");
        secondTrainee.setLastName("Doe");

        Trainee thirdTrainee = new Trainee();
        thirdTrainee.setFirstName("Joe");
        thirdTrainee.setLastName("Doe");

        Trainee fourthTrainee = new Trainee();
        fourthTrainee.setFirstName("Joe");
        fourthTrainee.setLastName("Doe");

        List<Trainee> existingTrainees = new ArrayList<>();

        when(userDao.findUsers(any(Trainee.class)))
                .thenAnswer(invocation -> new ArrayList<>(existingTrainees));

        when(userDao.save(any(Trainee.class)))
                .then(invocation -> {
                    Trainee savedTrainee = invocation.getArgument(0);
                    existingTrainees.add(savedTrainee);
                    return savedTrainee;
                });

        // Execution & Verification
        Trainee first = (Trainee) userCredentialsService.createCredentials(firstTrainee);
        Trainee second = (Trainee) userCredentialsService.createCredentials(secondTrainee);
        Trainee third = (Trainee) userCredentialsService.createCredentials(thirdTrainee);

        userDao.deleteById(thirdTrainee);

        Trainee fourth = (Trainee) userCredentialsService.createCredentials(fourthTrainee);


        verify(userDao, times(1)).deleteById(thirdTrainee);

        assertEquals("Joe.Doe", first.getUsername());
        assertEquals("Joe.Doe1", second.getUsername());
        assertEquals("Joe.Doe3", fourth.getUsername(), "Expected username for the fourth Trainee should be Joe.Doe3");
    }

    /**
     * Tests to create credentials for Trainer
     */

    @Test
    void testCreateTrainerWithUniqueUsername() {
        // Given
        Trainer newTrainer = new Trainer();
        newTrainer.setFirstName("Jane");
        newTrainer.setLastName("Smith");

        when(userDao.findUsers(any(Trainer.class)))
                .thenReturn(Collections.emptyList());
        when(userDao.save(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        User createdTrainer = userCredentialsService.createCredentials(newTrainer);

        // Then
        verify(userDao, times(1)).save(any(Trainer.class));
        assertNotNull(createdTrainer.getUsername(), "Username should not be null");
        assertTrue(createdTrainer.getUsername().startsWith("Jane.Smith"), "Username should start with 'Jane.Smith'");
        assertEquals(10, createdTrainer.getPassword().length(), "Password should be 10 characters long");
    }

    @Test
    void testCreateTrainerWithDuplicateUsername() {
        // Given
        Trainer existingTrainer = new Trainer();
        existingTrainer.setUsername("Jane.Smith");

        Trainer newTrainer = new Trainer();
        newTrainer.setFirstName("Jane");
        newTrainer.setLastName("Smith");

        when(userDao.findUsers(any(Trainer.class)))
                .thenReturn(List.of(existingTrainer));
        when(userDao.save(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        User createdTrainer = userCredentialsService.createCredentials(newTrainer);

        // Then
        verify(userDao, times(1)).save(any(Trainer.class));
        assertNotEquals("Jane.Smith", createdTrainer.getUsername(), "Username should not be 'Jane.Smith' to avoid duplication");
        assertEquals(10, createdTrainer.getPassword().length(), "Password should be 10 characters long");
    }

    @Test
    void testCreateSequentialTrainers() {
        // Setup
        List<User> existingTrainers = new ArrayList<>();
        Trainer firstTrainer = new Trainer();
        firstTrainer.setFirstName("Bob");
        firstTrainer.setLastName("Williams");

        Trainer secondTrainer = new Trainer();
        secondTrainer.setFirstName("Bob");
        secondTrainer.setLastName("Williams");

        Trainer thirdTrainer = new Trainer();
        thirdTrainer.setFirstName("Bob");
        thirdTrainer.setLastName("Williams");

        Trainer fourthTrainer = new Trainer();
        fourthTrainer.setFirstName("Bob");
        fourthTrainer.setLastName("Williams");

        when(userDao.findUsers(any(Trainer.class)))
                .thenAnswer(invocation -> new ArrayList<>(existingTrainers));
        when(userDao.save(any(Trainer.class)))
                .then(invocation -> {
                    Trainer savedTrainer = invocation.getArgument(0);
                    existingTrainers.add(savedTrainer);
                    return savedTrainer;
                });

        // Execution & Verification
        Trainer first = (Trainer) userCredentialsService.createCredentials(firstTrainer);
        Trainer second = (Trainer) userCredentialsService.createCredentials(secondTrainer);
        Trainer third = (Trainer) userCredentialsService.createCredentials(thirdTrainer);

        userDao.deleteById(thirdTrainer);

        Trainer fourth = (Trainer) userCredentialsService.createCredentials(fourthTrainer);

        verify(userDao, times(1)).deleteById(thirdTrainer);
        assertEquals("Bob.Williams", first.getUsername());
        assertEquals("Bob.Williams1", second.getUsername());
        assertEquals("Bob.Williams3", fourth.getUsername(), "Expected username for the fourth Trainee should be Bob.Williams3");
    }



}
