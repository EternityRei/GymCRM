package org.example.gymcrm.service.impl;

import jakarta.persistence.EntityNotFoundException;
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
        logger.debug("Creating trainer with name: {} {}, specialization: {}", firstName, lastName, specialization);

        String username = userCredentialsService.createUsername(firstName, lastName);
        String password = userCredentialsService.generateRandomPassword();

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(password);
        user.setActive(true);

        logger.debug("Validating user={}", user);
        validationService.validateEntity(user);

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(specialization);

        logger.debug("Validating created trainer={}", trainer);
        validationService.validateEntity(trainer);

        logger.info("Trainer={} was created successfully", trainer);
        trainerRepository.save(trainer);

        return trainer;
    }

    @Override
    @Authenticate
    public Trainer updateTrainer(String id, Trainer updatedTrainer) {
        logger.debug("Attempting to update trainer with ID: {}", id);

        Trainer existingTrainer = trainerRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found with ID: " + id));
        logger.debug("Trainer found with ID: {}. Proceeding with update.", id);

        initCredentials(updatedTrainer, existingTrainer);
        logger.debug("Credentials initialized for trainer with ID: {}", id);

        try {
            validationService.validateEntity(existingTrainer);
            trainerRepository.save(existingTrainer);
            logger.info("Trainer with ID: {} updated successfully.", id);
        } catch (Exception e) {
            logger.error("Failed to update trainer with ID: {}. Error: {}", id, e.getMessage());
        }

        return existingTrainer;
    }


    @Override
    @Authenticate
    public void updateTrainerPassword(String id, String newPassword) {
        logger.debug("Attempting to update password for trainer with ID: {}", id);
        try {
            userCredentialsService.updatePassword(id, newPassword);
            logger.info("Password for trainer with ID: {} updated successfully.", id);
        } catch (Exception e) {
            logger.error("Failed to update password for trainer with ID: {}. Error: {}", id, e.getMessage());
        }
    }


    @Override
    @Authenticate
    public void deactivateTrainerProfile(String id) {
        logger.debug("Attempting to deactivate trainer profile for ID: {}", id);

        try {
            boolean isDeactivated = userCredentialsService.banUser(id);
            if (isDeactivated) {
                logger.info("Trainer profile successfully deactivated for ID: {}", id);
            } else {
                logger.info("Trainer profile successfully activated for ID: {}", id);
            }
        } catch (Exception e) {
            logger.error("Error deactivating trainer profile for ID: {}", id, e);
        }
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
        logger.debug("Attempting to fetch trainer with ID: {}", id);
        Optional<Trainer> trainer = trainerRepository.findById(Long.valueOf(id));
        if (trainer.isPresent()) {
            logger.info("Trainer found with ID: {}", id);
        } else {
            logger.warn("No trainer found with ID: {}", id);
        }
        return trainer;
    }


    @Override
    @Authenticate
    public Optional<Trainer> getTrainerByUsername(String username) {
        logger.debug("Attempting to fetch trainer by username: {}", username);
        Optional<Trainer> trainer = trainerRepository.findByUsername(username);
        if (trainer.isPresent()) {
            logger.info("Trainer found with username: {}", username);
        } else {
            logger.warn("No trainer found with username: {}", username);
        }
        return trainer;
    }

    @Override
    @Authenticate
    public List<Trainer> getAllTrainers() {
        logger.debug("Fetching all trainers");
        List<Trainer> trainers = trainerRepository.findAll();
        logger.info("Found {} trainers", trainers.size());
        return trainers;
    }


    @Override
    @Authenticate
    public List<Training> getTrainerTrainings(String trainerUsername, Date from, Date to, String traineeName) {
        logger.info("Fetching trainings for " +
                "trainer: {}, " +
                "from: {}," +
                " to: {}," +
                " traineeName: {}",
                trainerUsername,
                from,
                to,
                traineeName
        );

        List<Training> trainings = trainingRepository
                .findAll(
                        TrainingSpecification
                                .byCriteria(
                                        null,
                                        trainerUsername,
                                        from,
                                        to,
                                        null,
                                        traineeName,
                                        null
                                )
                );

        logger.debug("Found {} trainings for trainerUsername: {}", trainings.size(), trainerUsername);
        return trainings;
    }


    @Override
    @Authenticate
    public List<Trainer> getTrainersNotAssignedToTraineeByUsername(String username) {
        logger.debug("Fetching trainers not assigned to trainee with username: {}", username);
        List<Trainer> trainers = trainerRepository.findTrainersNotAssignedToTraineeByUsername(username);
        logger.info("Found {} trainers not assigned to trainee with username: {}", trainers.size(), username);
        return trainers;
    }
}

