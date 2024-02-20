package org.example.gymcrm.service;

import org.example.gymcrm.model.Trainee;

import java.util.List;

public interface TraineeService {
    Trainee createTrainee(Trainee trainee);
    Trainee updateTrainee(String id, Trainee trainee);
    void deleteTrainee(String id);
    Trainee getTrainee(String id);
    List<Trainee> getAllTrainees();
}

