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
    void updateTrainerPassword(Trainer trainer, String newPassword);
    void updateTrainerProfileStatus(Trainer trainer);
    Optional<Trainer> getTrainerByUsername(String username, String password);
    List<Trainer> getAllTrainers();
    List<Training> getTrainerTrainings(Trainer trainer, Date from, Date to, String traineeName);
    List<Trainer> getTrainersNotAssignedToTraineeByUsername(String username, String password);
}

