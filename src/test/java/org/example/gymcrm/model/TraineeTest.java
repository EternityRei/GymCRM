package org.example.gymcrm.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TraineeTest {
    @Test
    void testConstructorWithParameters() {
        Trainee trainee = new Trainee("1", "Jane", "Doe", "janeDoe", "pass123", true, "1990-01-01", "123 Main St");
        assertEquals("1", trainee.getId());
        assertEquals("Jane", trainee.getFirstName());
        assertEquals("Doe", trainee.getLastName());
        assertEquals("janeDoe", trainee.getUsername());
        assertEquals("pass123", trainee.getPassword());
        assertEquals(true, trainee.isActive());
        assertEquals("1990-01-01", trainee.getDateOfBirth());
        assertEquals("123 Main St", trainee.getAddress());
    }

    @Test
    void testSetDateOfBirth() {
        Trainee trainee = new Trainee();
        trainee.setDateOfBirth("2000-12-31");
        assertEquals("2000-12-31", trainee.getDateOfBirth());
    }

    @Test
    void testSetAddress() {
        Trainee trainee = new Trainee();
        trainee.setAddress("456 Elm St");
        assertEquals("456 Elm St", trainee.getAddress());
    }

    @Test
    void testToString() {
        Trainee trainee = new Trainee("1", "Jane", "Doe", "janeDoe", "pass123", true, "1990-01-01", "123 Main St");
        String expectedToString = "Trainee{id='1', firstName='Jane', lastName='Doe', username='janeDoe', password='pass123', isActive=true, dateOfBirth='1990-01-01', address='123 Main St'}";
        assertEquals(expectedToString, trainee.toString());
    }
}
