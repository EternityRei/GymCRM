package org.example.gymcrm.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainerTest {
    @Test
    void testConstructorWithParameters() {
        Trainer trainer = new Trainer("1", "John", "Doe", "johnDoe", "pass123", true, "Fitness");
        assertEquals("1", trainer.getId());
        assertEquals("John", trainer.getFirstName());
        assertEquals("Doe", trainer.getLastName());
        assertEquals("johnDoe", trainer.getUsername());
        assertEquals("pass123", trainer.getPassword());
        assertEquals(true, trainer.isActive());
        assertEquals("Fitness", trainer.getSpecialization());
    }

    @Test
    void testSetSpecialization() {
        Trainer trainer = new Trainer();
        trainer.setSpecialization("Yoga");
        assertEquals("Yoga", trainer.getSpecialization());
    }

    @Test
    void testToString() {
        Trainer trainer = new Trainer("1", "John", "Doe", "johnDoe", "pass123", true, "Fitness");
        String expectedToString = "Trainer{id='1', firstName='John', lastName='Doe', username='johnDoe', password='pass123', isActive=true, specialization='Fitness'}";
        assertEquals(expectedToString, trainer.toString());
    }
}
