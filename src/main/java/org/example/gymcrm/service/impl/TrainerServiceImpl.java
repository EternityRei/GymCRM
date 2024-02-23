package org.example.gymcrm.service.impl;

import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.service.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerDao trainerDao;

    private final UserCredentialsGeneratorImpl userCredentialsService;
    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);


    @Autowired
    public TrainerServiceImpl(TrainerDao trainerDao, UserCredentialsGeneratorImpl userCredentialsService) {
        this.trainerDao = trainerDao;
        this.userCredentialsService = userCredentialsService;
    }

    public Trainer createTrainer(Trainer trainer) {
        String username = userCredentialsService.createUsername(trainer.getFirstName(),trainer.getLastName());
        String password = userCredentialsService.generateRandomPassword();

        trainer.setUsername(username);
        trainer.setPassword(password);
        logger.info("Trainer created with username: {}", username);
        return trainerDao.save(trainer);
    }

    @Override
    public Trainer updateTrainer(String id, Trainer trainer) {
        logger.info("Updating trainer with ID: {}", id);
        Trainer existingTrainer = trainerDao.findById(id);
        if (existingTrainer != null) {

            existingTrainer.setFirstName(trainer.getFirstName());
            existingTrainer.setLastName(trainer.getLastName());
            existingTrainer.setPassword(trainer.getPassword());
            existingTrainer.setActive(trainer.isActive());
            existingTrainer.setSpecialization(trainer.getSpecialization());

            trainerDao.save(existingTrainer);
            logger.info("Trainer with ID: {} updated successfully", id);
            return existingTrainer;
        } else {
            logger.error("Failed to update trainer. Trainer with ID: {} not found.", id);
            throw new RuntimeException("Trainer with ID " + id + " not found.");
        }
    }

    @Override
    public Trainer getTrainer(String id) {
        logger.info("Fetching trainer with ID: {}", id);
        return trainerDao.findById(id);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        logger.info("Fetching all trainers");
        return trainerDao.findAll();
    }
}

