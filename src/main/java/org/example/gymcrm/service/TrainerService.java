package org.example.gymcrm.service;

import org.example.gymcrm.model.Trainer;

import java.util.List;

public interface TrainerService {
    Trainer updateTrainer(String id, Trainer trainer);
    Trainer getTrainer(String id);
    List<Trainer> getAllTrainers();
}

