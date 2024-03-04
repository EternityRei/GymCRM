package org.example.gymcrm.repository;

import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.storage.TraineeStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TraineeRepositoryTest {
    @Mock
    private TraineeStorage traineeStorage;

    @InjectMocks
    private TraineeRepository traineeDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        Trainee trainee = new Trainee(); // Assuming Trainee has a default constructor
        when(traineeStorage.save(trainee)).thenReturn(trainee);

        Trainee result = traineeDao.save(trainee);

        verify(traineeStorage).save(trainee);
        assertEquals(trainee, result);
    }

    @Test
    void testFindById() {
        Trainee trainee = new Trainee(); // Setup
        when(traineeStorage.findById("1")).thenReturn(trainee);

        Trainee result = traineeDao.findById("1");

        verify(traineeStorage).findById("1");
        assertEquals(trainee, result);
    }

    @Test
    void testFindAll() {
        List<Trainee> trainees = Arrays.asList(new Trainee(), new Trainee());
        when(traineeStorage.findAll()).thenReturn(trainees);

        List<Trainee> result = traineeDao.findAll();

        verify(traineeStorage).findAll();
        assertEquals(trainees, result);
    }

    @Test
    void testDeleteById() {
        doNothing().when(traineeStorage).deleteById("1");

        traineeDao.deleteById("1");

        verify(traineeStorage).deleteById("1");
    }

    @Test
    void testFindByFirstNameAndLastNameStartingWith() {
        List<Trainee> trainees = Arrays.asList(new Trainee(), new Trainee());
        when(traineeStorage.findByFirstNameAndLastNameStartingWith("John", "D")).thenReturn(trainees);

        List<Trainee> result = traineeDao.findByFirstNameAndLastNameStartingWith("John", "D");

        verify(traineeStorage).findByFirstNameAndLastNameStartingWith("John", "D");
        assertEquals(trainees, result);
    }
}
