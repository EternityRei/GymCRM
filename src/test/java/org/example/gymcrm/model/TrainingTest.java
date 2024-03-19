package org.example.gymcrm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrainingTest {

    private Training training;
    private Trainer trainer;
    private Trainee trainee;
    private TrainingType trainingType;
    private Date trainingDate;

    @BeforeEach
    void setUp() {
        trainer = new Trainer(1L, "Fitness", new User()); // Assuming a User class exists
        trainee = new Trainee(); // Assuming default constructor is adequate for this test
        trainingType = new TrainingType(); // Assuming a default constructor and this is another entity
        trainingDate = new Date(); // Use a fixed date for consistency in real tests
        training = new Training(1L, trainer, trainee, trainingType, "Weightlifting", trainingDate, 60);
    }

    @Test
    void getAndSetTrainingId() {
        training.setTrainingId(2L);
        assertEquals(2L, training.getTrainingId());
    }

    @Test
    void getAndSetTrainer() {
        Trainer newTrainer = new Trainer(2L, "Yoga", new User()); // New trainer for testing
        training.setTrainer(newTrainer);
        assertEquals(newTrainer, training.getTrainer());
    }

    @Test
    void getAndSetTrainee() {
        Trainee newTrainee = new Trainee(); // New trainee for testing
        training.setTrainee(newTrainee);
        assertEquals(newTrainee, training.getTrainee());
    }

    @Test
    void getAndSetTrainingType() {
        TrainingType newTrainingType = new TrainingType(); // New training type for testing
        training.setTrainingType(newTrainingType);
        assertEquals(newTrainingType, training.getTrainingType());
    }

    @Test
    void getAndSetTrainingName() {
        training.setTrainingName("Cardio");
        assertEquals("Cardio", training.getTrainingName());
    }

    @Test
    void getAndSetTrainingDate() {
        Date newDate = new Date(); // Consider using a specific date for consistency
        training.setTrainingDate(newDate);
        assertEquals(newDate, training.getTrainingDate());
    }

    @Test
    void getAndSetTrainingDuration() {
        training.setTrainingDuration(90);
        assertEquals(90, training.getTrainingDuration());
    }

    @Test
    void testEqualsAndHashcode() {
        Training anotherTraining = new Training(1L, trainer, trainee, trainingType, "Weightlifting", trainingDate, 60);
        assertTrue(training.equals(anotherTraining) && anotherTraining.equals(training));
        assertEquals(training.hashCode(), anotherTraining.hashCode());
    }

    @Test
    void testToStringContainsRelevantInfo() {
        String toStringResult = training.toString();
        assertTrue(toStringResult.contains("Weightlifting") && toStringResult.contains("60"));
    }

}
