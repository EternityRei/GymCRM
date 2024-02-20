package org.example.gymcrm.service;

import org.example.gymcrm.model.Training;

import java.util.List;

public interface TrainingService {
    Training createTraining(Training training);
    Training getTraining(String id);
    List<Training> getAllTrainings();
}

