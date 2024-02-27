package org.example.gymcrm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TrainingTest {

    @Mock
    private Trainer mockTrainer;
    @Mock
    private Trainee mockTrainee;
    private TrainingType trainingType;
    private Training training;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainingType = new TrainingType("1", "Some Type");
        LocalDate localDate = LocalDate.parse("1999-11-23");
        Date trainingDate = java.sql.Date.valueOf(localDate);
        training = new Training("id1", mockTrainer, mockTrainee, trainingType, "Training Name", trainingDate, 60);
    }

    @Test
    void testTrainingProperties() {
        assertEquals("id1", training.getTrainingId());
        assertEquals(mockTrainer, training.getTrainer());
        assertEquals(mockTrainee, training.getTrainee());
        assertEquals(trainingType, training.getTrainingType());
        assertEquals("Training Name", training.getTrainingName());
        assertNotNull(training.getTrainingDate());
        assertEquals(60, training.getTrainingDuration());
    }

    @Test
    void testEquals() {
        Training anotherTraining = new Training("id1", mockTrainer, mockTrainee, trainingType, "Training Name", training.getTrainingDate(), 60);
        assertEquals(training, anotherTraining);
    }

    @Test
    void testHashCode() {
        Training anotherTraining = new Training("id1", mockTrainer, mockTrainee, trainingType, "Training Name", training.getTrainingDate(), 60);
        assertEquals(training.hashCode(), anotherTraining.hashCode());
    }

    @Test
    void testToString() {
        String expected = "Training{trainingId='id1', trainer=" + mockTrainer + ", trainee=" + mockTrainee + ", trainingType=" + trainingType + ", trainingName='Training Name', trainingDate=" + training.getTrainingDate() + ", trainingDuration=60}";
        assertEquals(expected, training.toString());
    }

    @Test
    void testSetTrainingId() {
        training.setTrainingId("id2");
        assertEquals("id2", training.getTrainingId());
    }

    @Test
    void testSetTrainer() {
        Trainer newTrainer = new Trainer(); // Assuming there's a default constructor
        training.setTrainer(newTrainer);
        assertEquals(newTrainer, training.getTrainer());
    }

    @Test
    void testSetTrainee() {
        Trainee newTrainee = new Trainee(); // Assuming there's a default constructor
        training.setTrainee(newTrainee);
        assertEquals(newTrainee, training.getTrainee());
    }


    @Test
    void testSetTrainingType() {
        TrainingType newTrainingType = new TrainingType("1", "name"); // Assuming ANOTHER_TYPE is another enum value
        training.setTrainingType(newTrainingType);
        assertEquals(newTrainingType, training.getTrainingType());
    }

    @Test
    void testSetTrainingName() {
        training.setTrainingName("New Training Name");
        assertEquals("New Training Name", training.getTrainingName());
    }

    @Test
    void testSetTrainingDate() {
        Date newDate = new Date();
        training.setTrainingDate(newDate);
        assertEquals(newDate, training.getTrainingDate());
    }

    @Test
    void testSetTrainingDuration() {
        training.setTrainingDuration(120);
        assertEquals(120, training.getTrainingDuration());
    }

}
