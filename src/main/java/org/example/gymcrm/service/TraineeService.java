package org.example.gymcrm.service;

import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.model.Training;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface TraineeService {
    Trainee createTrainee(Trainee trainee);
    Trainee updateTrainee(Trainee trainee);
    void updateTraineePassword(String id, String password);
    void updateTraineeProfileStatus(String id);
    void deleteTrainee(String id);
    void deleteTraineeByUsername(String username);
    Optional<Trainee> getTrainee(String id);
    Trainee getTraineeByUsername(String username);
    List<Trainee> getAllTrainees();
    List<Training> getTraineeTrainings(String traineeUsername, Date from, Date to, String trainerName, String trainingType);
    void addTrainersToTrainee(String traineeUsername, List<Long> newTrainerIds);
    Optional<Trainee> getTraineeByUsernameAuthentication(String username);
    Optional<Trainee> getTraineeAuthentication(String id);
}

