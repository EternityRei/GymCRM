package org.example.gymcrm.service.impl;

import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceImplTest {

    @Mock
    private TrainerDao trainerDao;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainer_SuccessfulCreation() {
        // Arrange
        Trainer newTrainer = new Trainer();
        newTrainer.setFirstName("Bob");
        newTrainer.setLastName("Williams");

        // Mocking DAO responses
        when(trainerDao.save(any(Trainer.class))).thenReturn(newTrainer);

        // Act
        Trainer createdTrainer = trainerService.createTrainer(newTrainer);

        // Assert
        assertNotNull(createdTrainer);
        verify(trainerDao, times(1)).save(any(Trainer.class));
        assertNotNull(createdTrainer.getUsername(), "Username should not be null");
        assertTrue(createdTrainer.getUsername().startsWith("Bob.Williams"), "Username should start with 'Bob.Williams'");
        assertEquals(10, createdTrainer.getPassword().length(), "Password should be 10 characters long");
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
}
