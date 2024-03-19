package org.example.gymcrm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeTest {
    private Trainee trainee;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(); // Assuming User is another entity class like Trainee
        user.setId(1L);
        trainee = new Trainee(1L, "1990-01-01", "123 Main St", user);
    }

    @Test
    void getDateOfBirth_ReturnsCorrectDate() {
        assertEquals("1990-01-01", trainee.getDateOfBirth());
    }

    @Test
    void getAddress_ReturnsCorrectAddress() {
        assertEquals("123 Main St", trainee.getAddress());
    }

    @Test
    void getUser_ReturnsCorrectUser() {
        assertEquals(user, trainee.getUser());
    }

    @Test
    void setAddress_SetsCorrectAddress() {
        trainee.setAddress("456 Elm St");
        assertEquals("456 Elm St", trainee.getAddress());
    }

    @Test
    void setTrainers_SetsCorrectTrainers() {
        HashSet<Trainer> trainers = new HashSet<>();
        trainers.add(new Trainer()); // Assuming Trainer is another entity class
        trainee.setTrainers(trainers);

        assertEquals(trainers, trainee.getTrainers());
    }


    @Test
    void testEquals_Symmetric() {
        User user = new User(); // Assuming a basic constructor or setter methods to set ID
        user.setId(1L);

        Trainee trainee1 = new Trainee(1L, "2000-01-01", "123 Main St", user);
        Trainee trainee2 = new Trainee(1L, "2000-01-01", "123 Main St", user);

        assertTrue(trainee1.equals(trainee2) && trainee2.equals(trainee1));
        assertEquals(trainee1.hashCode(), trainee2.hashCode());
    }

    @Test
    void testEquals_NullAndOtherClass() {
        User user = new User();
        user.setId(1L);

        Trainee trainee = new Trainee(1L, "2000-01-01", "123 Main St", user);

        assertNotEquals(trainee, null);
        assertNotEquals(trainee, new Object());
    }

    @Test
    void testHashCode_Consistency() {
        User user = new User();
        user.setId(1L);

        Trainee trainee = new Trainee(1L, "2000-01-01", "123 Main St", user);

        int expectedHashCode = trainee.hashCode();
        assertEquals(expectedHashCode, trainee.hashCode());
    }

    @Test
    void testToString_ContainsRelevantInfo() {
        User user = new User();
        user.setId(1L);

        Trainee trainee = new Trainee(1L, "2000-01-01", "123 Main St", user);

        String toStringResult = trainee.toString();
        assertTrue(toStringResult.contains("2000-01-01") && toStringResult.contains("123 Main St"));
    }
}
