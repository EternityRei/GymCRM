package org.example.gymcrm.service;

import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.Training;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface TraineeService {
    Trainee createTrainee(Trainee trainee);
    Trainee updateTrainee(Trainee trainee);
    void updateTraineePassword(Trainee trainee, String newPassword);
    void updateTraineeProfileStatus(Trainee trainee);
    void deleteTrainee(String id);
    void deleteTraineeByUsername(String username, String password);
    Optional<Trainee> getTrainee(String id);
    Trainee getTraineeByUsername(String username, String password);
    List<Trainee> getAllTrainees();
    List<Training> getTraineeTrainings(Trainee trainee, Date from, Date to, String trainerName, String trainingType);
    void addTrainersToTrainee(Trainee trainee, List<Trainer> newTrainers);
}

