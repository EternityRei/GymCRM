package org.example.gymcrm.service.impl;

import org.example.gymcrm.dao.TrainingDao;
import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.Training;
import org.example.gymcrm.model.TrainingType;
import org.example.gymcrm.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceImplTest {

    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTraining_WhenTrainingDoesNotExist() {
        // Arrange
        Training newTraining = new Training();
        newTraining.setTrainingId("3w773132-81a2-4f14-b88e-3ba79e19a0db");
        when(trainingDao.findById(anyString())).thenReturn(null);
        when(trainingDao.save(any(Training.class))).thenReturn(newTraining);

        // Act
        Training createdTraining = trainingService.createTraining(newTraining);

        // Assert
        assertNotNull(createdTraining);
        assertEquals("3w773132-81a2-4f14-b88e-3ba79e19a0db", createdTraining.getTrainingId());
    }

    @Test
    void createTraining_WhenTrainingExists_ThrowsException() {
        // Arrange
        Training existingTraining = new Training();
        existingTraining.setTrainingId("89726452-81a2-4f14-b88e-3ba79e19a0db");
        when(trainingDao.findById("89726452-81a2-4f14-b88e-3ba79e19a0db")).thenReturn(existingTraining);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> trainingService.createTraining(existingTraining));
    }

    @Test
    void getTraining_ReturnsCorrectTraining() {
        // Arrange
        String id = "89726452-81a2-4f14-b88e-3ba79e19a0db";

        Training expectedTraining = new Training(
                "89726452-81a2-4f14-b88e-3ba79e19a0db",
                new Trainer(
                        "3ca22c2e-9f02-4a3c-814a-2c8c8d5d3317",
                        "Alice",
                        "Johnson",
                        "Alice.Johnson",
                        "alicej123",
                        true,
                        "Strength Training"
                ),
                new Trainee(
                        "990aac1f-94d3-475b-ae15-ce41b3087c08",
                        "Bob",
                        "Smith",
                        "Bob.Smith",
                        "securepass",
                        false,
                        "1990-07-15",
                        "456 Elm St"
                ),
                new TrainingType("1", "Pass"),
                "Advanced Strength",
                Date.valueOf("2024-03-15"),
                21

        );
        when(trainingDao.findById(id)).thenReturn(expectedTraining);

        // Act
        Training actualTraining = trainingService.getTraining(id);

        // Assert
        assertEquals(expectedTraining, actualTraining);
    }

    @Test
    void getAllTrainings_ReturnsAllTrainings() {
        // Arrange
        List<Training> expectedTrainings = Arrays.asList(new Training(), new Training());
        when(trainingDao.findAll()).thenReturn(expectedTrainings);

        // Act
        List<Training> actualTrainings = trainingService.getAllTrainings();

        // Assert
        assertFalse(actualTrainings.isEmpty(), "Expected non-empty list of trainings");
        assertEquals(expectedTrainings.size(), actualTrainings.size(), "Expected list sizes to match");
    }
}
