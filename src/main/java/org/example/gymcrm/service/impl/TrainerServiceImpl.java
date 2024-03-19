package org.example.gymcrm.service.impl;

import org.example.gymcrm.annotation.Authenticate;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.Training;
import org.example.gymcrm.model.User;
import org.example.gymcrm.repository.TrainerRepository;
import org.example.gymcrm.repository.TrainingRepository;
import org.example.gymcrm.service.TrainerService;
import org.example.gymcrm.service.ValidationService;
import org.example.gymcrm.specification.TrainingSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;

    private final UserServiceImpl userCredentialsService;
    private final TrainingRepository trainingRepository;
    private final ValidationService validationService;
    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);


    @Autowired
    public TrainerServiceImpl(TrainerRepository trainerRepository, UserServiceImpl userCredentialsService, TrainingRepository trainingRepository, ValidationService validationService) {
        this.trainerRepository = trainerRepository;
        this.userCredentialsService = userCredentialsService;
        this.trainingRepository = trainingRepository;
        this.validationService = validationService;
    }

    @Override
    public Trainer createTrainer(String firstName, String lastName, String specialization) {
        String username = userCredentialsService.createUsername(firstName, lastName);
        String password = userCredentialsService.generateRandomPassword();

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(password);
        user.setActive(true);

        validationService.validateEntity(user);

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(specialization);

        validationService.validateEntity(trainer);

        return trainer;
    }

    @Override
    @Authenticate
    public Trainer updateTrainer(String id, Trainer trainer) {
        logger.info("Updating trainer with ID: {}", id);
        Trainer existingTrainer = trainerRepository.findById(Long.valueOf(id)).orElseThrow();
        initCredentials(trainer, existingTrainer);

        validationService.validateEntity(existingTrainer);

        trainerRepository.save(existingTrainer);
        logger.info("Trainer with ID: {} updated successfully", id);
        return existingTrainer;
    }

    @Override
    @Authenticate
    public void updateTrainerPassword(String id, String newPassword) {
        logger.info("Updating trainer password");
        userCredentialsService.updatePassword(id, newPassword);
    }

    @Override
    @Authenticate
    public void deactivateTrainerProfile(String id) {
        logger.info("Changing trainer account status");
        userCredentialsService.banUser(id);
    }

    private void initCredentials(Trainer trainer, Trainer existingTrainer) {
        existingTrainer.getUser().setFirstName(trainer.getUser().getFirstName());
        existingTrainer.getUser().setLastName(trainer.getUser().getLastName());
        existingTrainer.getUser().setActive(trainer.getUser().isActive());
        existingTrainer.setSpecialization(trainer.getSpecialization());
    }

    @Override
    @Authenticate
    public Optional<Trainer> getTrainer(String id) {
        logger.info("Fetching trainer with ID: {}", id);
        return trainerRepository.findById(Long.valueOf(id));
    }

    @Override
    @Authenticate
    public Optional<Trainer> getTrainerByUsername(String username) {
        logger.info("Fetching trainer by username={}", username);
        return trainerRepository.findByUsername(username);
    }

    @Override
    @Authenticate
    public List<Trainer> getAllTrainers() {
        logger.info("Fetching all trainers");
        return trainerRepository.findAll();
    }

    @Override
    @Authenticate
    public List<Training> getTrainerTrainings(String trainerUsername, Date from, Date to, String traineeName) {
        Specification<Training> spec = TrainingSpecification.byCriteria(null, trainerUsername, from, to, null, traineeName, null);
        return trainingRepository.findAll(spec);
    }

    @Override
    @Authenticate
    public List<Trainer> getTrainersNotAssignedToTraineeByUsername(String username) {
        return trainerRepository.findTrainersNotAssignedToTraineeByUsername(username);
    }
}

