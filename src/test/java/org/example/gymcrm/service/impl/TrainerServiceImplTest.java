package org.example.gymcrm.service.impl;

import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.Training;
import org.example.gymcrm.model.TrainingType;
import org.example.gymcrm.model.User;
import org.example.gymcrm.repository.TrainerRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private UserServiceImpl userCredentialsService;
    @Mock
    private ValidationService validationService;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Test
    void whenCreateTrainer_thenSuccess() {
        String firstName = "John";
        String lastName = "Doe";
        TrainingType specialization = new TrainingType(1L, "Fitness");

        when(userCredentialsService.createUsername(firstName, lastName)).thenReturn("johndoe");
        when(userCredentialsService.generateRandomPassword()).thenReturn("randomPassword");

        Trainer createdTrainer = trainerService.createTrainer(firstName, lastName, specialization);

        assertNotNull(createdTrainer);
        assertEquals("Fitness", createdTrainer.getSpecialization());
        assertEquals("johndoe", createdTrainer.getUser().getUsername());
        verify(validationService, times(1)).validateEntity(any(Trainer.class));
    }

    @Test
    void whenUpdateTrainerNotFound_thenThrowException() {
        String id = "1";
        Trainer updatedTrainer = new Trainer();

        when(trainerRepository.findById(Long.valueOf(id))).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> trainerService.updateTrainer(updatedTrainer));
    }

    @Test
    void whenDeactivateTrainerProfile_thenSuccess() {
        String id = "1";

        doNothing().when(userCredentialsService).modifyAccountStatus(id);

        trainerService.updateTrainerProfileStatus(id);

        verify(userCredentialsService, times(1)).modifyAccountStatus(id);
    }

    @Test
    void whenGetTrainerByUsernameFound_thenSuccess() {
        String username = "trainerUsername";
        Trainer expectedTrainer = new Trainer();
        expectedTrainer.setUser(new User());
        expectedTrainer.getUser().setUsername(username);

        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(expectedTrainer));

        Optional<Trainer> result = trainerService.getTrainerByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUser().getUsername());
    }

    @Test
    void whenGetAllTrainers_thenSuccess() {
        List<Trainer> expectedTrainers = List.of(new Trainer(), new Trainer());
        when(trainerRepository.findAll()).thenReturn(expectedTrainers);

        List<Trainer> result = trainerService.getAllTrainers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedTrainers, result);
    }

    @Test
    void whenGetTrainerTrainings_thenSuccess() {
        String trainerUsername = "trainerUsername";
        Date from = Date.valueOf("2023-01-01");
        Date to = Date.valueOf("2023-01-31");
        List<Training> expectedTrainings = List.of(new Training(), new Training());

        when(trainingRepository.findAll(any(Specification.class))).thenReturn(expectedTrainings);

        List<Training> result = trainerService.getTrainerTrainings(trainerUsername, from, to, "TraineeName");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedTrainings, result);
    }

    @Test
    void whenGetTrainersNotAssignedToTraineeByUsername_thenSuccess() {
        String username = "traineeUsername";
        List<Trainer> expectedTrainers = List.of(new Trainer(), new Trainer());

        when(trainerRepository.findTrainersNotAssignedToTraineeByUsername(username)).thenReturn(expectedTrainers);

        List<Trainer> result = trainerService.getTrainersNotAssignedToTraineeByUsername(username);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedTrainers, result);
    }

    @Test
    void whenUpdateTrainerPasswordAndTrainerNotFound_thenThrowException() {
        String id = "1";
        doThrow(new RuntimeException("Trainer not found")).when(userCredentialsService).updatePassword(id, "newPassword");

        assertThrows(RuntimeException.class, () -> trainerService.updateTrainerPassword(id, "newPassword"));
    }

}
