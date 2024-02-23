package org.example.gymcrm.service.impl;

import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dao.UserDao;
import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.User;
import org.example.gymcrm.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceImplTest {

    private TrainerDao trainerDao;
    private UserDao userDao;
    private UserCredentialsGeneratorImpl userCredentialsGenerator;
    private TrainerServiceImpl trainerService;

    private List<User> existingUsers;

    @BeforeEach
    public void setUp() {
        trainerDao = Mockito.mock(TrainerDao.class);
        userDao = Mockito.mock(UserDao.class);
        userCredentialsGenerator = new UserCredentialsGeneratorImpl(userDao);

        trainerService = new TrainerServiceImpl(trainerDao, userCredentialsGenerator);

        existingUsers = findAllExistingUsers();
    }

    @Test
    public void testCreateTrainerWithUniqueUsername() {
        // Given
        Trainer createdTrainer = new Trainer();
        createdTrainer.setId(String.valueOf(UUID.randomUUID()));
        createdTrainer.setFirstName("Jane");
        createdTrainer.setLastName("Doe");


        // When
        when(userDao.findUsers()).thenReturn(existingUsers);

        when(trainerDao.save(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Trainer resultTrainer = trainerService.createTrainer(createdTrainer);

        // Then
        assertEquals("Jane.Doe1", resultTrainer.getUsername());
        verify(trainerDao).save(createdTrainer);
    }

    @Test
    public void testCreateTraineeWithDuplicateUsername() {
        // Given
        Trainer mockTrainee = new Trainer();
        mockTrainee.setId(String.valueOf(UUID.randomUUID()));
        mockTrainee.setFirstName("John");
        mockTrainee.setLastName("Smith");

        // Simulate the scenario where the initial username is taken and a unique one is generated
        when(userDao.findUsers()).thenReturn(existingUsers);

        when(trainerDao.save(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Trainer resultTrainee = trainerService.createTrainer(mockTrainee);

        // Then
        verify(trainerDao).save(mockTrainee);
        assertEquals("John.Smith", resultTrainee.getUsername(), "Expected a unique username with a suffix.");
    }

    @Test
    void testCreateSequentialTrainees() {
        // Given
        Trainer firstTrainee = new Trainer();
        firstTrainee.setId(String.valueOf(UUID.randomUUID()));
        firstTrainee.setFirstName("Milly");
        firstTrainee.setLastName("Brown");

        Trainer secondTrainee = new Trainer();
        secondTrainee.setId(String.valueOf(UUID.randomUUID()));
        secondTrainee.setFirstName("Milly");
        secondTrainee.setLastName("Brown");

        Trainer thirdTrainee = new Trainer();
        thirdTrainee.setId(String.valueOf(UUID.randomUUID()));
        thirdTrainee.setFirstName("Milly");
        thirdTrainee.setLastName("Brown");

        Trainer fourthTrainee = new Trainer();
        fourthTrainee.setId(String.valueOf(UUID.randomUUID()));
        fourthTrainee.setFirstName("Milly");
        fourthTrainee.setLastName("Brown");

        when(userDao.findUsers()).thenReturn(existingUsers);

        when(trainerDao.save(any(Trainer.class))).thenAnswer(new Answer<Trainer>() {
            public Trainer answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return (Trainer) args[0];
            }
        });

        Trainer first = trainerService.createTrainer(firstTrainee);
        existingUsers.add(first);
        Trainer second = trainerService.createTrainer(secondTrainee);
        existingUsers.add(second);
        Trainer third = trainerService.createTrainer(thirdTrainee);
        existingUsers.add(third);

        // Simulate deletion of the third trainee
        doNothing().when(trainerDao).deleteById(third.getId());

        // Assume deletion of the third trainee's username happens externally, affecting subsequent username generation
        Trainer fourth = trainerService.createTrainer(fourthTrainee);

        // Then
        verify(trainerDao, times(4)).save(any(Trainer.class)); // Ensure save was called for each trainee

        assertEquals("Milly.Brown", first.getUsername());
        assertEquals("Milly.Brown1", second.getUsername());
        assertEquals("Milly.Brown3", fourth.getUsername(), "Expected username for the fourth Trainee should be Joe.Doe3 after deletion.");
    }

    @Test
    void updateTrainer_ExistingTrainer_SuccessfulUpdate() {
        // Arrange
        String trainerId = "1de82c4e-e4c4-4d58-b3f6-5d9e2a412c88";
        Trainer existingTrainer = new Trainer(
                "1de82c4e-e4c4-4d58-b3f6-5d9e2a412c88",
                "Bob",
                "Williams",
                "Bob.Williams",
                "alicej123",
                true,
                "Strength Training");

        Trainer updatedTrainer = new Trainer(
                "1de82c4e-e4c4-4d58-b3f6-5d9e2a412c88",
                "Bob",
                "Williams",
                "Bob.Williams",
                "alicej123",
                true,
                "Yoga");

        when(trainerDao.findById(trainerId)).thenReturn(existingTrainer);
        when(trainerDao.save(any(Trainer.class))).thenReturn(updatedTrainer);

        // Act
        Trainer result = trainerService.updateTrainer(trainerId, updatedTrainer);

        // Assert
        assertNotNull(result);
        assertEquals(updatedTrainer.getFirstName(), result.getFirstName());
        assertEquals(updatedTrainer.getLastName(), result.getLastName());
        assertEquals(updatedTrainer.getUsername(), result.getUsername());
        assertEquals(updatedTrainer.getPassword(), result.getPassword());
        assertEquals(updatedTrainer.isActive(), result.isActive());
        assertEquals(updatedTrainer.getSpecialization(), result.getSpecialization());
        verify(trainerDao).save(any(Trainer.class));
    }

    @Test
    void updateTrainer_NonExistingTrainer_ThrowsException() {
        // Arrange
        String nonExistingTrainerId = "nonExistingId";
        when(trainerDao.findById(nonExistingTrainerId)).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> trainerService.updateTrainer(nonExistingTrainerId, new Trainer()));
    }

    @Test
    void getTrainer_ReturnsTrainer() {
        // Arrange
        String trainerId = "1de82c4e-e4c4-4d58-b3f6-5d9e2a412c88";
        Trainer existingTrainer = new Trainer(
                "1de82c4e-e4c4-4d58-b3f6-5d9e2a412c88",
                "Bob",
                "Williams",
                "Bob.Williams",
                "alicej123",
                true,
                "Strength Training");

        when(trainerDao.findById(trainerId)).thenReturn(existingTrainer);

        // Act
        Trainer actualTrainer = trainerService.getTrainer(trainerId);

        // Assert
        assertEquals(existingTrainer, actualTrainer);
    }

    @Test
    void getAllTrainers_ReturnsListOfTrainers() {
        // Arrange
        List<Trainer> expectedTrainers = Arrays.asList(new Trainer(), new Trainer());
        when(trainerDao.findAll()).thenReturn(expectedTrainers);

        // Act
        List<Trainer> actualTrainers = trainerService.getAllTrainers();

        // Assert
        assertFalse(actualTrainers.isEmpty(), "Expected non-empty list of trainers");
        assertEquals(expectedTrainers.size(), actualTrainers.size(), "Expected list sizes to match");
    }

    private List<User> findAllExistingUsers(){
        Trainer trainer = new Trainer();
        trainer.setId("2bc140d7-5873-4a26-ac89-1a131781df18");
        trainer.setFirstName("Jane");
        trainer.setLastName("Doe");
        trainer.setUsername("Jane.Doe");
        trainer.setPassword("password123");
        trainer.setActive(true);
        trainer.setSpecialization("Cardio");

        Trainer trainer1 = new Trainer();
        trainer1.setId("990aac1f-94d3-475b-ae15-ce41b3087c08");
        trainer1.setFirstName("Bob");
        trainer1.setLastName("Smith");
        trainer1.setUsername("Bob.Smith");
        trainer1.setPassword("securepass456");
        trainer1.setActive(false);
        trainer1.setSpecialization("Pass");

        Trainer trainer2 = new Trainer();
        trainer2.setId("3bc140d7-5873-4a26-ac89-1a131781df18");
        trainer2.setFirstName("Alice");
        trainer2.setLastName("Johnson");
        trainer2.setUsername("Alice.Johnson");
        trainer2.setPassword("pass123");
        trainer2.setActive(true);
        trainer2.setSpecialization("Kio");

        List<User> existingUsers = new ArrayList<>();
        existingUsers.add(trainer);
        existingUsers.add(trainer1);
        existingUsers.add(trainer2);

        return existingUsers;
    }
}
