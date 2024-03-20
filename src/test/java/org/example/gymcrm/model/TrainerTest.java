package org.example.gymcrm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrainerTest {
    private Trainer trainer;
    private User user;
    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        user = new User(); // Assuming User is another entity class like Trainer
        user.setId(1L);
        trainingType = new TrainingType(1L, "Fitness");
        trainer = new Trainer(1L, trainingType, user);
    }

    @Test
    void getId_ReturnsCorrectId() {
        assertEquals(1L, trainer.getId());
    }

    @Test
    void getSpecialization_ReturnsCorrectSpecialization() {
        assertEquals("Fitness", trainer.getSpecialization().getName());
    }

    @Test
    void getUser_ReturnsCorrectUser() {
        assertEquals(user, trainer.getUser());
    }

    @Test
    void setSpecialization_SetsCorrectSpecialization() {
        trainer.setSpecialization(new TrainingType(2L, "Yoga"));
        assertEquals("Yoga", trainer.getSpecialization().getName());
    }

    @Test
    void testEquals_Symmetric() {
        Trainer anotherTrainer = new Trainer(1L, trainingType, user);
        assertTrue(trainer.equals(anotherTrainer) && anotherTrainer.equals(trainer));
        assertEquals(trainer.hashCode(), anotherTrainer.hashCode());
    }

    @Test
    void testToString_ContainsRelevantInfo() {
        String toStringResult = trainer.toString();
        assertTrue(toStringResult.contains("Fitness") && toStringResult.contains(user.toString()));
    }
}
