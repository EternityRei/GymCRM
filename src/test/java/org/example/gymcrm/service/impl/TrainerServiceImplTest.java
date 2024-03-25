package org.example.gymcrm.service.impl;

import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.Training;
import org.example.gymcrm.model.TrainingType;
import org.example.gymcrm.model.User;
import org.example.gymcrm.repository.TrainerRepository;
import org.example.gymcrm.repository.TrainingRepository;
import org.example.gymcrm.repository.TrainingTypeRepository;
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
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private TrainingTypeRepository trainingTypeRepository;
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
        // Mock the trainingTypeRepository to return the expected specialization
        when(trainingTypeRepository.findByName(specialization.getName())).thenReturn(Optional.of(specialization));

        Trainer createdTrainer = trainerService.createTrainer(firstName, lastName, specialization);

        assertNotNull(createdTrainer);
        assertEquals("Fitness", createdTrainer.getSpecialization().getName());
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
        Trainer trainer = new Trainer();
        trainer.setUser(new User());
        trainer.getUser().setId(1L);

        // Assuming the account status change is successful and returns true, for example
        when(userCredentialsService.modifyAccountStatus(String.valueOf(1L))).thenReturn(true);

        trainerService.updateTrainerProfileStatus(trainer);

        verify(userCredentialsService, times(1)).modifyAccountStatus(String.valueOf(1L));
    }


    @Test
    void whenGetTrainerByUsernameFound_thenSuccess() {
        String username = "trainerUsername";
        String password = "password";

        Trainer expectedTrainer = new Trainer();
        expectedTrainer.setUser(new User());
        expectedTrainer.getUser().setUsername(username);

        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(expectedTrainer));

        Optional<Trainer> result = trainerService.getTrainerByUsername(username, password);

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
        Trainer trainer = new Trainer();
        trainer.setUser(new User());
        trainer.getUser().setUsername(trainerUsername);

        Date from = Date.valueOf("2023-01-01");
        Date to = Date.valueOf("2023-01-31");
        List<Training> expectedTrainings = List.of(new Training(), new Training());

        when(trainingRepository.findAll(any(Specification.class))).thenReturn(expectedTrainings);

        List<Training> result = trainerService.getTrainerTrainings(trainer, from, to, "TraineeName");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedTrainings, result);
    }

    @Test
    void whenGetTrainersNotAssignedToTraineeByUsername_thenSuccess() {
        String username = "traineeUsername";
        String password = "pass";
        List<Trainer> expectedTrainers = List.of(new Trainer(), new Trainer());

        when(trainerRepository.findTrainersNotAssignedToTraineeByUsername(username)).thenReturn(expectedTrainers);

        List<Trainer> result = trainerService.getTrainersNotAssignedToTraineeByUsername(username, password);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedTrainers, result);
    }

    @Test
    void whenUpdateTrainerPasswordAndTrainerNotFound_thenNoExceptionThrown() {
        Trainer trainer = new Trainer();
        trainer.setUser(new User());
        trainer.getUser().setId(1L);

        doThrow(new RuntimeException("Trainer not found"))
                .when(userCredentialsService)
                .updatePassword(String.valueOf(trainer.getUser().getId()), "newPassword");

        // Test that no exception is thrown when attempting to update the password.
        assertDoesNotThrow(() -> trainerService.updateTrainerPassword(trainer, "newPassword"));

        verify(userCredentialsService).updatePassword(String.valueOf(trainer.getUser().getId()), "newPassword");
    }

    @Test
    void whenUpdateTrainer_thenSuccess() {
        Long trainerId = 1L;
        Trainer existingTrainer = new Trainer();
        existingTrainer.setId(trainerId);
        User user = new User();
        user.setFirstName("Joe");
        user.setLastName("Doe");
        existingTrainer.setUser(user);

        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);
        trainingType.setName("Fitness");
        existingTrainer.setSpecialization(trainingType);

        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setId(trainerId);
        updatedTrainer.setUser(new User());
        updatedTrainer.getUser().setFirstName("Joe");
        updatedTrainer.getUser().setLastName("Doe");
        updatedTrainer.setSpecialization(trainingType);

        // Apply lenient stubbing
        lenient().when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(existingTrainer));
        lenient().when(trainingTypeRepository.findByName(trainingType.getName())).thenReturn(Optional.of(trainingType));
        lenient().when(trainerRepository.save(any(Trainer.class))).thenReturn(existingTrainer);

        Trainer result = trainerService.updateTrainer(updatedTrainer);

        assertNotNull(result);
        assertEquals("Joe", result.getUser().getFirstName());
        assertEquals("Doe", result.getUser().getLastName());
        assertEquals(trainingType.getName(), result.getSpecialization().getName());

        // Verifies that save method is indeed called which utilizes the stubbing
        verify(trainerRepository, times(1)).save(any(Trainer.class));
        // Ensure validation is indeed called
        verify(validationService, times(1)).validateEntity(any(Trainer.class));
    }

    @Test
    void whenUpdateTrainerPassword_thenSuccess() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setId(1L); // Assuming the user has been set with an ID
        trainer.setUser(user);
        String newPassword = "newSecurePassword";

        // Correct way to mock the behavior for a void return type method
        doNothing().when(userCredentialsService).updatePassword(String.valueOf(user.getId()), newPassword);

        // Act - no exception should be thrown
        assertDoesNotThrow(() -> trainerService.updateTrainerPassword(trainer, newPassword));

        // Assert
        verify(userCredentialsService, times(1)).updatePassword(String.valueOf(trainer.getUser().getId()), newPassword);
    }
}
