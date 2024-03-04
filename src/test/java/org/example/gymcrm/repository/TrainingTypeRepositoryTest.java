package org.example.gymcrm.repository;

import org.example.gymcrm.model.TrainingType;
import org.example.gymcrm.storage.TrainingTypeStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TrainingTypeRepositoryTest {
    @Mock
    private TrainingTypeStorage trainingTypeStorage;

    @InjectMocks
    private TrainingTypeRepository trainingTypeDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        TrainingType trainingType = new TrainingType(); // Assuming TrainingType has a default constructor
        when(trainingTypeStorage.findById("1")).thenReturn(trainingType);

        TrainingType result = trainingTypeDao.findById("1");

        verify(trainingTypeStorage).findById("1");
        assertEquals(trainingType, result);
    }

    @Test
    void testFindAll() {
        List<TrainingType> trainingTypes = Arrays.asList(new TrainingType(), new TrainingType());
        when(trainingTypeStorage.findAll()).thenReturn(trainingTypes);

        List<TrainingType> result = trainingTypeDao.findAll();

        verify(trainingTypeStorage).findAll();
        assertEquals(trainingTypes, result);
    }
}
