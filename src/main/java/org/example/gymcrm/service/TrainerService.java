package org.example.gymcrm.service;

import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.Training;
import org.example.gymcrm.model.TrainingType;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface TrainerService {
    Trainer createTrainer(String firstName, String lastName, TrainingType specialization);
    Trainer updateTrainer(Trainer trainer);
    void updateTrainerPassword(String id, String newPassword);
    void updateTrainerProfileStatus(String id);
    Optional<Trainer> getTrainer(String id);
    Optional<Trainer> getTrainerAuthentication(String id);
    Optional<Trainer> getTrainerByUsername(String username);
    Optional<Trainer> getTrainerByUsernameAuthenticate(String username);
    List<Trainer> getAllTrainers();
    List<Training> getTrainerTrainings(String trainerUsername, Date from, Date to, String traineeName);
    List<Trainer> getTrainersNotAssignedToTraineeByUsername(String username);
}

