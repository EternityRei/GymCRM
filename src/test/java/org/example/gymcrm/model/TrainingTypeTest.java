package org.example.gymcrm.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainingTypeTest {
    @Test
    public void testConstructor() {
        TrainingType trainingType = new TrainingType("1", "Yoga");
        assertEquals("1", trainingType.getId());
        assertEquals("Yoga", trainingType.getName());
    }

    @Test
    public void testSetId() {
        TrainingType trainingType = new TrainingType();
        trainingType.setId("2");
        assertEquals("2", trainingType.getId());
    }

    @Test
    public void testSetName() {
        TrainingType trainingType = new TrainingType();
        trainingType.setName("Pilates");
        assertEquals("Pilates", trainingType.getName());
    }
}
