package org.example.gymcrm.service.impl;

import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.User;
import org.example.gymcrm.repository.TraineeRepository;
import org.example.gymcrm.repository.TrainerRepository;
import org.example.gymcrm.service.ValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TraineeServiceImplTest {

    @Mock
    private UserServiceImpl userCredentialsGenerator;

    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @Test
    void whenCreateTrainee_thenSuccess() {
        Trainee inputTrainee = new Trainee();
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        inputTrainee.setUser(user);

        when(userCredentialsGenerator.createUsername(anyString(), anyString())).thenReturn("johndoe");
        when(userCredentialsGenerator.generateRandomPassword()).thenReturn("randomPassword");
        when(traineeRepository.save(any(Trainee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Trainee createdTrainee = traineeService.createTrainee(inputTrainee);

        assertNotNull(createdTrainee);
        assertEquals("johndoe", createdTrainee.getUser().getUsername());
        assertEquals("randomPassword", createdTrainee.getUser().getPassword());
        verify(validationService, times(1)).validateEntity(any(User.class));
    }

    @Test
    void whenCreateTraineeWithInvalidUser_thenValidationFails() {
        Trainee inputTrainee = new Trainee();
        inputTrainee.setUser(new User()); // Assume this User object is invalid

        doThrow(RuntimeException.class).when(validationService).validateEntity(any(User.class));

        assertThrows(RuntimeException.class, () -> traineeService.createTrainee(inputTrainee));
    }

    @Test
    void whenGetTraineeByUsernameNotFound_thenEmptyResult() {
        String username = "nonexistent";
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

        Trainee result = traineeService.getTraineeByUsername(username);

        assertNotNull(result);
    }

    @Test
    void whenUpdateTrainee_thenSuccess() {
        String traineeId = "1";
        Trainee existingTrainee = new Trainee();
        User existingUser = new User();
        existingUser.setUsername("existingUsername");
        existingTrainee.setUser(existingUser);

        Trainee updatedTrainee = new Trainee();
        User updatedUser = new User();
        updatedUser.setFirstName("NewFirstName");
        updatedUser.setLastName("NewLastName");
        updatedTrainee.setUser(updatedUser);

        when(traineeRepository.findById(Long.valueOf(traineeId))).thenReturn(Optional.of(existingTrainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(existingTrainee);

        Trainee result = traineeService.updateTrainee(updatedTrainee);

        assertNotNull(result);
        assertEquals("NewFirstName", result.getUser().getFirstName());
        assertEquals("NewLastName", result.getUser().getLastName());

        // Adjusted to verify the validation of the Trainee object, not just the User object
        verify(validationService, times(1)).validateEntity(any(Trainee.class));
    }


    @Test
    void whenDeleteTraineeByUsernameNotFound_thenThrowException() {
        String username = "unknown";
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> traineeService.deleteTraineeByUsername(username));
        assertTrue(exception.getMessage().contains("Trainee not found"));
    }


    @Test
    void whenAddTrainersToTrainee_thenSuccess() {
        String traineeUsername = "traineeUsername";
        Trainee trainee = new Trainee();
        trainee.setTrainers(new HashSet<>()); // Assume trainee starts with no trainers

        List<Long> newTrainerIds = Arrays.asList(1L, 2L);
        Trainer trainer1 = new Trainer();
        trainer1.setId(1L);
        Trainer trainer2 = new Trainer();
        trainer2.setId(2L);

        when(traineeRepository.findByUsername(traineeUsername)).thenReturn(Optional.of(trainee));
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer1));
        when(trainerRepository.findById(2L)).thenReturn(Optional.of(trainer2));

        traineeService.addTrainersToTrainee(traineeUsername, newTrainerIds);

        assertEquals(2, trainee.getTrainers().size());
        assertTrue(trainee.getTrainers().contains(trainer1) && trainee.getTrainers().contains(trainer2));
    }

    @Test
    void whenDeactivateTraineeProfile_thenSuccess() {
        String id = "1";
        doNothing().when(userCredentialsGenerator).modifyAccountStatus(anyString());

        // Execution
        traineeService.updateTraineeProfileStatus(id);

        // Verification
        verify(userCredentialsGenerator, times(1)).modifyAccountStatus(id);
    }

    @Test
    void whenGetTraineeFound_thenSuccess() {
        Long id = 1L;
        Trainee expectedTrainee = new Trainee();
        expectedTrainee.setId(id);

        when(traineeRepository.findById(id)).thenReturn(Optional.of(expectedTrainee));

        Optional<Trainee> result = traineeService.getTrainee(String.valueOf(id));

        assertTrue(result.isPresent());
        assertEquals(expectedTrainee, result.get());
    }

    @Test
    void whenGetTraineeNotFound_thenEmptyOptional() {
        Long id = 1L;
        when(traineeRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Trainee> result = traineeService.getTrainee(String.valueOf(id));

        assertFalse(result.isPresent());
    }


    @Test
    void whenGetAllTrainees_thenSuccess() {
        List<Trainee> expectedTrainees = Arrays.asList(new Trainee(), new Trainee());
        when(traineeRepository.findAll()).thenReturn(expectedTrainees);

        List<Trainee> result = traineeService.getAllTrainees();

        assertNotNull(result);
        assertEquals(expectedTrainees.size(), result.size());
    }

    @Test
    void whenDeleteTraineeWithInvalidId_thenThrowException() {
        doThrow(new RuntimeException("Database error")).when(traineeRepository).deleteById(anyLong());

        assertThrows(RuntimeException.class, () -> traineeService.deleteTrainee("999"));
    }


}
