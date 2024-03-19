package org.example.gymcrm.service;

import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.Training;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface TrainerService {
    Trainer createTrainer(String firstName, String lastName, String specialization);
    Trainer updateTrainer(String id, Trainer trainer);
    void updateTrainerPassword(String id, String newPassword);
    void deactivateTrainerProfile(String id);
    Optional<Trainer> getTrainer(String id);
    Optional<Trainer> getTrainerByUsername(String username);
    List<Trainer> getAllTrainers();
    public List<Training> getTrainerTrainings(String trainerUsername, Date from, Date to, String traineeName);
    List<Trainer> getTrainersNotAssignedToTraineeByUsername(String username);
}

