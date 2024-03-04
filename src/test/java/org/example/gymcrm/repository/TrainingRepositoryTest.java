package org.example.gymcrm.repository;

import org.example.gymcrm.model.Training;
import org.example.gymcrm.storage.TrainingStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TrainingRepositoryTest {
    @Mock
    private TrainingStorage trainingStorage;

    @InjectMocks
    private TrainingRepository trainingDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        Training training = new Training(); // Assuming Training has a default constructor
        when(trainingStorage.save(training)).thenReturn(training);

        Training result = trainingDao.save(training);

        verify(trainingStorage).save(training);
        assertEquals(training, result);
    }

    @Test
    void testFindById() {
        Training training = new Training(); // Setup
        when(trainingStorage.findById("1")).thenReturn(training);

        Training result = trainingDao.findById("1");

        verify(trainingStorage).findById("1");
        assertEquals(training, result);
    }

    @Test
    void testFindAll() {
        List<Training> trainings = Arrays.asList(new Training(), new Training());
        when(trainingStorage.findAll()).thenReturn(trainings);

        List<Training> result = trainingDao.findAll();

        verify(trainingStorage).findAll();
        assertEquals(trainings, result);
    }

    @Test
    void testDeleteById() {
        doNothing().when(trainingStorage).deleteById("1");

        trainingDao.deleteById("1");

        verify(trainingStorage).deleteById("1");
    }
}
