package org.example.gymcrm.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.Training;
import org.example.gymcrm.model.User;
import org.example.gymcrm.repository.TraineeRepository;
import org.example.gymcrm.repository.TrainerRepository;
import org.example.gymcrm.repository.TrainingRepository;
import org.example.gymcrm.repository.TrainingTypeRepository;
import org.example.gymcrm.service.ValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Date;
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
    private TrainingRepository trainingRepository;

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
    void whenGetTraineeByUsernameNotFound_thenExpectEntityNotFoundException() {
        String username = "nonexistent";
        String password = "nonexistent";
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            traineeService.getTraineeByUsername(username, password);
        });
    }


    @Test
    void whenUpdateTrainee_thenSuccess() {
        Long traineeId = 1L; // Use Long to match the expected type of ID
        Trainee existingTrainee = new Trainee();
        existingTrainee.setId(traineeId); // Ensure the existingTrainee has an ID
        User existingUser = new User();
        existingUser.setUsername("existingUsername");
        existingTrainee.setUser(existingUser);

        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setId(traineeId); // Set the ID to match the existingTrainee
        User updatedUser = new User();
        updatedUser.setFirstName("NewFirstName");
        updatedUser.setLastName("NewLastName");
        updatedTrainee.setUser(updatedUser);

        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(existingTrainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(existingTrainee);

        Trainee result = traineeService.updateTrainee(updatedTrainee);

        assertNotNull(result);
        assertEquals("NewFirstName", result.getUser().getFirstName());
        assertEquals("NewLastName", result.getUser().getLastName());

        // Verify the validation of the Trainee object, not just the User object
        verify(validationService, times(1)).validateEntity(any(Trainee.class));
    }


    @Test
    void whenDeleteTraineeByUsernameNotFound_thenNoExceptionThrown() {
        String username = "unknown";
        String password = "infnef";
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Execute
        assertDoesNotThrow(() -> traineeService.deleteTraineeByUsername(username, password));
    }



    @Test
    void whenAddTrainersToTrainee_thenSuccess() {
        // Setup
        String traineeUsername = "traineeUsername";
        Trainee trainee = new Trainee();
        trainee.setUser(new User());
        trainee.getUser().setUsername(traineeUsername);
        trainee.getUser().setPassword("pass");
        trainee.setTrainers(new HashSet<>()); // Assume trainee starts with no trainers

        Trainer trainer1 = new Trainer();
        trainer1.setId(1L);
        Trainer trainer2 = new Trainer();
        trainer2.setId(2L);
        List<Trainer> newTrainers = Arrays.asList(trainer1, trainer2);

        when(traineeRepository.findByUsername(traineeUsername)).thenReturn(Optional.of(trainee));
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer1));
        when(trainerRepository.findById(2L)).thenReturn(Optional.of(trainer2));

        // Act
        traineeService.addTrainersToTrainee(trainee, newTrainers);

        // Assert
        assertEquals(2, trainee.getTrainers().size());
        assertTrue(trainee.getTrainers().containsAll(newTrainers));
    }


    @Test
    void whenDeactivateTraineeProfile_thenSuccess() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setId(1L); // Assuming User has an ID field
        trainee.setUser(user);
        when(userCredentialsGenerator.modifyAccountStatus(String.valueOf(trainee.getUser().getId()))).thenReturn(true); // Assuming modifyAccountStatus returns a boolean

        // Execution
        traineeService.updateTraineeProfileStatus(trainee);

        // Verification
        verify(userCredentialsGenerator, times(1)).modifyAccountStatus(String.valueOf(trainee.getUser().getId()));
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
    void whenDeleteTraineeWithInvalidId_thenNoExceptionThrown() {
        doThrow(new RuntimeException("Database error")).when(traineeRepository).deleteById(anyLong());

        // Execute the method and verify it does not throw any exception
        assertDoesNotThrow(() -> traineeService.deleteTrainee("999"));

        // Additional verification that the repository's deleteById was called can be included if needed
        verify(traineeRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void whenUpdateTraineePassword_thenSuccess() {
        Long traineeId = 1L;
        Trainee trainee = new Trainee();
        trainee.setId(traineeId);
        User user = new User();
        user.setId(traineeId);
        user.setUsername("testUser");
        user.setPassword("oldPassword");
        trainee.setUser(user);
        String newPassword = "newPassword";

        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(trainee));

        // Assuming the userCredentialsGenerator.updatePassword does not return a value
        assertDoesNotThrow(() -> traineeService.updateTraineePassword(trainee, newPassword));

        verify(userCredentialsGenerator, times(1)).updatePassword(String.valueOf(user.getId()), newPassword);
    }


    @Test
    void whenUpdateTraineeProfileStatus_thenSuccess() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setId(2L);
        trainee.setUser(user);

        // Assuming the userCredentialsGenerator.modifyAccountStatus returns true for success
        when(userCredentialsGenerator.modifyAccountStatus(String.valueOf(user.getId()))).thenReturn(true);

        assertDoesNotThrow(() -> traineeService.updateTraineeProfileStatus(trainee));

        verify(userCredentialsGenerator, times(1)).modifyAccountStatus(String.valueOf(user.getId()));
    }

    @Test
    void whenGetTraineeTrainings_thenSuccess() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setUsername("traineeUser");
        trainee.setUser(user);
        Date from = Date.valueOf("2020-01-01");
        Date to = Date.valueOf("2020-12-31");
        String trainerName = "trainerName";
        String trainingType = "trainingType";

        List<Training> expectedTrainings = List.of(new Training());
        when(trainingRepository.findAll(any(Specification.class))).thenReturn(expectedTrainings);

        List<Training> trainings = traineeService.getTraineeTrainings(trainee, from, to, trainerName, trainingType);

        assertNotNull(trainings);
        assertFalse(trainings.isEmpty());
        assertEquals(expectedTrainings.size(), trainings.size());
    }

}
