package org.example.gymcrm.service;

import org.example.gymcrm.model.Training;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface TrainingService {
    Training createTraining(Training training);
    Optional<Training> getTraining(String id);
    List<Training> getAllTrainings();
    List<Training> findTrainings(String traineeUsername, String trainerUsername, Date start, Date end, String trainerName, String traineeName, String trainingType);
}

