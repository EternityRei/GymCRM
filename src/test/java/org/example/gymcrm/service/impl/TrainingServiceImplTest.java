package org.example.gymcrm.service.impl;

import jakarta.validation.ConstraintViolationException;
import org.example.gymcrm.model.Training;
import org.example.gymcrm.repository.TrainingRepository;
import org.example.gymcrm.service.ValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Test
    void whenCreateTraining_thenSuccess() {
        Training training = new Training();
        // Assume training is properly initialized

        when(trainingRepository.save(any(Training.class))).thenReturn(training);

        Training createdTraining = trainingService.createTraining(training);

        assertNotNull(createdTraining);
        verify(validationService, times(1)).validateEntity(training);
        verify(trainingRepository, times(1)).save(training);
    }

    @Test
    void whenGetTrainingFound_thenSuccess() {
        Long id = 1L;
        Optional<Training> expectedTraining = Optional.of(new Training());
        when(trainingRepository.findById(id)).thenReturn(expectedTraining);

        Optional<Training> result = trainingService.getTraining(String.valueOf(id));

        assertTrue(result.isPresent());
        assertEquals(expectedTraining, result);
    }

    @Test
    void whenGetAllTrainings_thenSuccess() {
        List<Training> expectedTrainings = List.of(new Training(), new Training());
        when(trainingRepository.findAll()).thenReturn(expectedTrainings);

        List<Training> result = trainingService.getAllTrainings();

        assertNotNull(result);
        assertEquals(expectedTrainings.size(), result.size());
        assertEquals(expectedTrainings, result);
    }

    @Test
    void whenFindTrainingsWithCriteria_thenSuccess() {
        List<Training> expectedTrainings = List.of(new Training());
        when(trainingRepository.findAll(any(Specification.class))).thenReturn(expectedTrainings);

        Date start = Date.valueOf("2023-01-01");
        Date end = Date.valueOf("2023-01-31");
        List<Training> result = trainingService.findTrainings("traineeUsername", "trainerUsername", start, end, "TrainerName", "TraineeName", "TrainingType");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedTrainings, result);
        verify(trainingRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void whenCreateTrainingWithInvalidData_thenValidationFails() {
        Training invalidTraining = new Training(); // Assume this training has invalid data
        doThrow(new RuntimeException("Validation failed")).when(validationService).validateEntity(invalidTraining);

        assertThrows(RuntimeException.class, () -> trainingService.createTraining(invalidTraining));
        verify(trainingRepository, never()).save(any(Training.class));
    }

    @Test
    void whenGetTrainingNotFound_thenEmptyResult() {
        Long id = 99L; // Assume no training with this ID exists
        when(trainingRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Training> result = trainingService.getTraining(String.valueOf(id));

        assertFalse(result.isPresent());
    }

    @Test
    void whenGetAllTrainingsAndNoneExist_thenEmptyList() {
        when(trainingRepository.findAll()).thenReturn(List.of());

        List<Training> result = trainingService.getAllTrainings();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void whenCreateTrainingViolatesConstraints_thenHandleException() {
        Training trainingWithConstraintsViolated = new Training(); // This training violates some constraints
        // Simulate a constraint violation exception
        doThrow(new ConstraintViolationException("Constraint violation", Set.of())).when(validationService).validateEntity(trainingWithConstraintsViolated);

        assertThrows(ConstraintViolationException.class, () -> trainingService.createTraining(trainingWithConstraintsViolated));
    }
    @Test
    void whenFindTrainingsWithInvalidCriteria_thenHandleErrorGracefully() {
        // Assuming invalid criteria that would cause an error in specification
        Date start = Date.valueOf("2023-03-01");
        Date end = Date.valueOf("2023-02-01"); // End date before start date as an example of invalid input

        when(trainingRepository.findAll(any(Specification.class))).thenThrow(new IllegalArgumentException("Invalid date range"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> trainingService.findTrainings("username", "trainerUsername", start, end, "TrainerName", "TraineeName", "TrainingType"));
        assertEquals("Invalid date range", exception.getMessage());
    }

    @Test
    void whenCreateTrainingWithInvalidName_thenValidationFails() {
        Training trainingWithInvalidName = new Training();
        trainingWithInvalidName.setTrainingName(""); // Invalid due to @NotBlank constraint

        // Simulate validation failure
        doThrow(new RuntimeException("Training name is a mandatory field")).when(validationService).validateEntity(trainingWithInvalidName);

        assertThrows(RuntimeException.class, () -> trainingService.createTraining(trainingWithInvalidName));
        verify(trainingRepository, never()).save(any(Training.class));
    }

    @Test
    void whenCreateTrainingWithNullDate_thenValidationFails() {
        Training trainingWithNullDate = new Training();
        trainingWithNullDate.setTrainingDate(null); // Invalid due to @NotNull constraint

        // Simulate validation failure
        doThrow(new RuntimeException("Training date is a mandatory field")).when(validationService).validateEntity(trainingWithNullDate);

        assertThrows(RuntimeException.class, () -> trainingService.createTraining(trainingWithNullDate));
        verify(trainingRepository, never()).save(any(Training.class));
    }

    @Test
    void whenCreateTrainingWithNullDuration_thenValidationFails() {
        Training trainingWithNullDuration = new Training();
        trainingWithNullDuration.setTrainingDuration(null); // Invalid due to @NotNull constraint

        // Simulate validation failure
        doThrow(new RuntimeException("Training duration is a mandatory field")).when(validationService).validateEntity(trainingWithNullDuration);

        assertThrows(RuntimeException.class, () -> trainingService.createTraining(trainingWithNullDuration));
        verify(trainingRepository, never()).save(any(Training.class));
    }


}
