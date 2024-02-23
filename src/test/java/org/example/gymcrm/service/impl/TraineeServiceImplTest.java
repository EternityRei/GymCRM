package org.example.gymcrm.service.impl;

import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.UserDao;
import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.model.User;
import org.example.gymcrm.service.impl.TraineeServiceImpl;
import org.example.gymcrm.storage.TraineeStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TraineeServiceImplTest {

    private TraineeDao traineeDao;
    private UserDao userDao;
    private UserCredentialsGeneratorImpl userCredentialsGenerator;
    private TraineeServiceImpl traineeService;

    private List<User> existingUsers;

    @BeforeEach
    public void setUp() {
        traineeDao = Mockito.mock(TraineeDao.class);
        userDao = Mockito.mock(UserDao.class);
        userCredentialsGenerator = new UserCredentialsGeneratorImpl(userDao);

        traineeService = new TraineeServiceImpl(traineeDao, userCredentialsGenerator);

        existingUsers = findAllExistingUsers();
    }

    @Test
    public void testCreateTraineeWithUniqueUsername() {
        // Given
        Trainee mockTrainee = new Trainee();
        mockTrainee.setId(String.valueOf(UUID.randomUUID()));
        mockTrainee.setFirstName("Jane");
        mockTrainee.setLastName("Doe");


        // When
        when(userDao.findUsers()).thenReturn(existingUsers);

        when(traineeDao.save(any(Trainee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Trainee resultTrainee = traineeService.createTrainee(mockTrainee);

        // Then
        assertEquals("Jane.Doe1", resultTrainee.getUsername());
        verify(traineeDao).save(mockTrainee);
    }

    @Test
    public void testCreateTraineeWithDuplicateUsername() {
        // Given
        Trainee mockTrainee = new Trainee();
        mockTrainee.setId(String.valueOf(UUID.randomUUID()));
        mockTrainee.setFirstName("John");
        mockTrainee.setLastName("Smith");

        // Simulate the scenario where the initial username is taken and a unique one is generated
        when(userDao.findUsers()).thenReturn(existingUsers);

        when(traineeDao.save(any(Trainee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Trainee resultTrainee = traineeService.createTrainee(mockTrainee);

        // Then
        verify(traineeDao).save(mockTrainee);
        assertEquals("John.Smith", resultTrainee.getUsername(), "Expected a unique username with a suffix.");
    }

    @Test
    void testCreateSequentialTrainees() {
        // Given
        Trainee firstTrainee = new Trainee();
        firstTrainee.setId(String.valueOf(UUID.randomUUID()));
        firstTrainee.setFirstName("Joe");
        firstTrainee.setLastName("Doe");

        Trainee secondTrainee = new Trainee();
        secondTrainee.setId(String.valueOf(UUID.randomUUID()));
        secondTrainee.setFirstName("Joe");
        secondTrainee.setLastName("Doe");

        Trainee thirdTrainee = new Trainee();
        thirdTrainee.setId(String.valueOf(UUID.randomUUID()));
        thirdTrainee.setFirstName("Joe");
        thirdTrainee.setLastName("Doe");

        Trainee fourthTrainee = new Trainee();
        fourthTrainee.setId(String.valueOf(UUID.randomUUID()));
        fourthTrainee.setFirstName("Joe");
        fourthTrainee.setLastName("Doe");

        when(userDao.findUsers()).thenReturn(existingUsers);

        when(traineeDao.save(any(Trainee.class))).thenAnswer(new Answer<Trainee>() {
            public Trainee answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return (Trainee) args[0];
            }
        });

        Trainee first = traineeService.createTrainee(firstTrainee);
        existingUsers.add(first);
        Trainee second = traineeService.createTrainee(secondTrainee);
        existingUsers.add(second);
        Trainee third = traineeService.createTrainee(thirdTrainee);
        existingUsers.add(third);

        // Simulate deletion of the third trainee
        doNothing().when(traineeDao).deleteById(third.getId());

        // Assume deletion of the third trainee's username happens externally, affecting subsequent username generation
        Trainee fourth = traineeService.createTrainee(fourthTrainee);

        // Then
        verify(traineeDao, times(4)).save(any(Trainee.class)); // Ensure save was called for each trainee

        assertEquals("Joe.Doe", first.getUsername());
        assertEquals("Joe.Doe1", second.getUsername());
        assertEquals("Joe.Doe3", fourth.getUsername(), "Expected username for the fourth Trainee should be Joe.Doe3 after deletion.");
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

    private List<User> findAllExistingUsers(){
        Trainee firstTrainee = new Trainee();
        firstTrainee.setId("2bc140d7-5873-4a26-ac89-1a131781df18");
        firstTrainee.setFirstName("Jane");
        firstTrainee.setLastName("Doe");
        firstTrainee.setUsername("Jane.Doe");
        firstTrainee.setPassword("password123");
        firstTrainee.setActive(true);
        firstTrainee.setDateOfBirth("1995-04-23");
        firstTrainee.setAddress("123 Main St, Anytown, Anystate");

        Trainee secondTrainee = new Trainee();
        secondTrainee.setId("990aac1f-94d3-475b-ae15-ce41b3087c08");
        secondTrainee.setFirstName("Bob");
        secondTrainee.setLastName("Smith");
        secondTrainee.setUsername("Bob.Smith");
        secondTrainee.setPassword("securepass456");
        secondTrainee.setActive(false);
        secondTrainee.setDateOfBirth("1990-07-15");
        secondTrainee.setAddress("456 Elm St, Othertown, Otherstate");

        Trainee thirdTrainee = new Trainee();
        thirdTrainee.setId("3bc140d7-5873-4a26-ac89-1a131781df18");
        thirdTrainee.setFirstName("Alice");
        thirdTrainee.setLastName("Johnson");
        thirdTrainee.setUsername("Alice.Johnson");
        thirdTrainee.setPassword("pass123");
        thirdTrainee.setActive(true);
        thirdTrainee.setDateOfBirth("1998-09-10");
        thirdTrainee.setAddress("789 Oak St, Somewhere, Anotherstate");

        List<User> existingUsers = new ArrayList<>();
        existingUsers.add(firstTrainee);
        existingUsers.add(secondTrainee);
        existingUsers.add(thirdTrainee);

        return existingUsers;
    }

}
