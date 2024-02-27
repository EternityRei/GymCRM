package org.example.gymcrm.storage;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.Training;
import org.example.gymcrm.model.TrainingType;
import org.example.gymcrm.storage.TrainingStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingStorageTest {

    @Mock
    private Resource initialTrainingFile;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TrainingStorage trainingStorage;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        String json = "[{" +
                "\"trainingId\": \"771bba03-8a22-4d76-83d8-389a9404d920\"," +
                "\"trainer\": {" +
                "\"id\": \"5f4f3af3-a423-4d5b-8b76-5f41f24ea85b\"," +
                "\"firstName\": \"John\"," +
                "\"lastName\": \"Doe\"," +
                "\"username\": \"John.Doe\"," +
                "\"password\": \"johndoe123\"," +
                "\"active\": true," +
                "\"specialization\": \"Cardio\"" +
                "}," +
                "\"trainee\": {" +
                "\"id\": \"2bc140d7-5873-4a26-ac89-1a131781df18\"," +
                "\"firstName\": \"Jane\"," +
                "\"lastName\": \"Doe\"," +
                "\"username\": \"Jane.Doe\"," +
                "\"password\": \"password\"," +
                "\"active\": true," +
                "\"dateOfBirth\": \"1995-04-23\"," +
                "\"address\": \"123 Main St\"" +
                "}," +
                "\"trainingType\": {" +
                "\"id\": \"1\"," +
                "\"name\": \"Cardio\"" +
                "}," +
                "\"trainingName\": \"Cardio Basics\"," +
                "\"trainingDate\": \"2024-03-01\"," +
                "\"trainingDuration\": \"34\"" +
                "}]";

        Trainer trainer = new Trainer("5f4f3af3-a423-4d5b-8b76-5f41f24ea85b", "John", "Doe", "John.Doe", "johndoe123", true, "Cardio");
        Trainee trainee = new Trainee("2bc140d7-5873-4a26-ac89-1a131781df18", "Jane", "Doe", "Jane.Doe", "password", true, "1995-04-23", "123 Main St");
        TrainingType trainingType = new TrainingType("1", "Cardio");

        Training training = new Training();
        training.setTrainingId("771bba03-8a22-4d76-83d8-389a9404d920");
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingType(trainingType);
        training.setTrainingName("Cardio Basics");
        training.setTrainingDate(Date.valueOf("2024-03-01"));
        training.setTrainingDuration(34);

        when(initialTrainingFile.getInputStream()).thenReturn(new ByteArrayInputStream(json.getBytes()));
        when(objectMapper.readValue((JsonParser) any(), any(TypeReference.class))).thenReturn(List.of(training));

        trainingStorage.init();
    }

    @Test
    void testSave() {
        Training training = new Training();
        training.setTrainingId(String.valueOf(UUID.randomUUID()));
        training.setTrainer(null);
        training.setTrainee(null);
        training.setTrainingType(new TrainingType("1", "Cardio"));
        training.setTrainingName("Cardio Basics");
        training.setTrainingDate(Date.valueOf("2024-03-01"));
        training.setTrainingDuration(60);

        when(trainingStorage.save(training)).thenReturn(training);

        assertEquals(training, savedTraining);

        assertEquals(training, trainingStorage.findById("2"));
    }

    @Test
    void testFindById() {
        Training training = trainingStorage.findById("1");
        assertNotNull(training);
        assertEquals("1", training.getTrainingId());
    }

    @Test
    void testFindAll() {
        List<Training> allTrainings = trainingStorage.findAll();
        assertFalse(allTrainings.isEmpty());
        assertTrue(allTrainings.stream().anyMatch(t -> "1".equals(t.getTrainingId())));
    }

    @Test
    void testDeleteById() {
        trainingStorage.deleteById("1");
        assertNull(trainingStorage.findById("1"));
    }
}
