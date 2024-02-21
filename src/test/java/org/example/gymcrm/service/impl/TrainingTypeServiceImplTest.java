package org.example.gymcrm.service.impl;

import org.example.gymcrm.dao.TrainingTypeDao;
import org.example.gymcrm.dao.UserDao;
import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.Training;
import org.example.gymcrm.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

public class TrainingTypeServiceImplTest {
    @Mock
    private TrainingTypeDao trainingTypeDao;

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTrainingType_ReturnsCorrectTraining() {
        // Arrange
        String id = "2";

        TrainingType expectedTrainingType = new TrainingType("2", "Advanced Stretching");

        when(trainingTypeDao.findById(id)).thenReturn(expectedTrainingType);

        // Act
        TrainingType actualTrainingType = trainingTypeService.findById(id);

        // Assert
        assertEquals(expectedTrainingType, actualTrainingType);
    }

    @Test
    void getAllTrainings_ReturnsAllTrainings() {
        // Arrange
        List<TrainingType> expectedTrainingTypes = Arrays.asList(new TrainingType(), new TrainingType());
        when(trainingTypeDao.findAll()).thenReturn(expectedTrainingTypes);

        // Act
        List<TrainingType> actualTrainings = trainingTypeService.findAll();

        // Assert
        assertFalse(actualTrainings.isEmpty(), "Expected non-empty list of trainings");
        assertEquals(expectedTrainingTypes.size(), actualTrainings.size(), "Expected list sizes to match");
    }
}
