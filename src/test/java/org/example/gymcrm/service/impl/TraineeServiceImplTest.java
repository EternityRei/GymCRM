package org.example.gymcrm.service.impl;

import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TraineeServiceImplTest {

    @Mock
    private TraineeDao traineeDao;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateTrainee_Successful() {
        Trainee existingTrainee = new Trainee(
                "2bc140d7-5873-4a26-ac89-1a131781df18",
                "Jane",
                "Doe",
                "Jane.Doe",
                "password123",
                true,
                "1995-04-23",
                "123 Main St, Anytown, Anystate");

        Trainee updatedTrainee = new Trainee(
                "2bc140d7-5873-4a26-ac89-1a131781df18",
                "Jane",
                "Doe",
                "Jane.Doe",
                "bebebe",
                true,
                "1995-04-23",
                "123 Main St, Anytown, Anystate");
        when(traineeDao.findById("2bc140d7-5873-4a26-ac89-1a131781df18")).thenReturn(existingTrainee);

        Trainee result = traineeService.updateTrainee("2bc140d7-5873-4a26-ac89-1a131781df18", updatedTrainee);

        assertNotNull(result);
        assertEquals(updatedTrainee.getFirstName(), result.getFirstName());
        assertEquals(updatedTrainee.getLastName(), result.getLastName());
        assertEquals(updatedTrainee.getUsername(), result.getUsername());
        assertEquals(updatedTrainee.getPassword(), result.getPassword());
        assertEquals(updatedTrainee.isActive(), result.isActive());
        assertEquals(updatedTrainee.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(updatedTrainee.getAddress(), result.getAddress());
        verify(traineeDao).save(any(Trainee.class));
    }

    @Test
    void updateTrainee_TraineeNotFound() {
        Trainee updatedTrainee = new Trainee(
                "id2",
                "Jane",
                "Doe",
                "Jane.Doe",
                "bebebe",
                true,
                "1995-04-23",
                "123 Main St, Anytown, Anystate");
        when(traineeDao.findById("id2")).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            traineeService.updateTrainee("id2", updatedTrainee);
        });

        String expectedMessage = "Trainee with ID id2 not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteTrainee_WhenTraineeExists() {
        // Arrange
        String traineeId = "2bc140d7-5873-4a26-ac89-1a131781df18";
        Trainee existingTrainee = new Trainee(
                "2bc140d7-5873-4a26-ac89-1a131781df18",
                "Jane",
                "Doe",
                "Jane.Doe",
                "password123",
                true,
                "1995-04-23",
                "123 Main St, Anytown, Anystate");
        when(traineeDao.findById(traineeId)).thenReturn(existingTrainee);

        // Act
        traineeService.deleteTrainee(traineeId);

        // Assert
        verify(traineeDao, times(1)).deleteById(traineeId);
    }

    @Test
    void deleteTrainee_WhenTraineeDoesNotExist_ThrowsException() {
        // Arrange
        String nonExistingId = "nonExistingId";
        when(traineeDao.findById(nonExistingId)).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> traineeService.deleteTrainee(nonExistingId));

        verify(traineeDao, never()).deleteById(anyString());
    }

    @Test
    void getTrainee_ReturnsCorrectTrainee_WhenTraineeExists() {
        // Arrange
        String traineeId = "2bc140d7-5873-4a26-ac89-1a131781df18";
        Trainee expectedTrainee = new Trainee(
                "2bc140d7-5873-4a26-ac89-1a131781df18",
                "Jane",
                "Doe",
                "Jane.Doe",
                "password123",
                true,
                "1995-04-23",
                "123 Main St, Anytown, Anystate");
        when(traineeDao.findById(traineeId)).thenReturn(expectedTrainee);

        // Act
        Trainee actualTrainee = traineeService.getTrainee(traineeId);

        // Assert
        assertEquals(expectedTrainee, actualTrainee, "The returned trainee should match the expected one.");
    }

    @Test
    void getAllTrainees_ReturnsListOfTrainees_WhenTraineesExist() {
        Trainee trainee1 = new Trainee(
                "2bc140d7-5873-4a26-ac89-1a131781df18",
                "Jane",
                "Doe",
                "Jane.Doe",
                "password123",
                true,
                "1995-04-23",
                "123 Main St, Anytown, Anystate");

        Trainee trainee2 = new Trainee(
                "990aac1f-94d3-475b-ae15-ce41b3087c08",
                "Bob",
                "Smith",
                "Bob.Smith",
                "securepass456",
                false,
                "1990-07-15",
                "456 Elm St, Othertown, Otherstate");
        // Arrange
        List<Trainee> expectedTrainees = Arrays.asList(new Trainee(), new Trainee()); // Assuming a list of trainees
        when(traineeDao.findAll()).thenReturn(expectedTrainees);

        // Act
        List<Trainee> actualTrainees = traineeService.getAllTrainees();

        // Assert
        assertFalse(actualTrainees.isEmpty(), "The returned list should not be empty.");
        assertEquals(expectedTrainees.size(), actualTrainees.size(), "The size of the returned list should match the expected list.");
        assertEquals(expectedTrainees, actualTrainees, "The returned list should match the expected list.");
    }

}
