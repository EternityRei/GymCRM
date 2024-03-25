package org.example.gymcrm.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.gymcrm.annotation.Authenticate;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.Training;
import org.example.gymcrm.model.TrainingType;
import org.example.gymcrm.model.User;
import org.example.gymcrm.repository.TrainerRepository;
import org.example.gymcrm.repository.TrainingRepository;
import org.example.gymcrm.repository.TrainingTypeRepository;
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
    private final TrainingTypeRepository trainingTypeRepository;
    private final ValidationService validationService;
    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);


    @Autowired
    public TrainerServiceImpl(TrainerRepository trainerRepository, UserServiceImpl userCredentialsService, TrainingRepository trainingRepository, TrainingTypeRepository trainingTypeRepository, ValidationService validationService) {
        this.trainerRepository = trainerRepository;
        this.userCredentialsService = userCredentialsService;
        this.trainingRepository = trainingRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.validationService = validationService;
    }

    @Override
    @Transactional
    public Trainer createTrainer(String firstName, String lastName, TrainingType specialization) {
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
        TrainingType managedSpecialization = trainingTypeRepository.findByName(specialization.getName()).orElseThrow();
        trainer.setSpecialization(managedSpecialization);

        logger.debug("Validating created trainer={}", trainer);
        validationService.validateEntity(trainer);

        logger.info("Trainer={} was created successfully", trainer);
        trainerRepository.save(trainer);

        return trainer;
    }

    @Override
    @Authenticate
    public Trainer updateTrainer(Trainer updatedTrainer) {
        logger.debug("Attempting to update trainer with ID: {}", updatedTrainer.getId());

        Trainer existingTrainer = trainerRepository.findById(updatedTrainer.getId())
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found with ID: " + updatedTrainer.getId()));
        logger.debug("Trainer found with ID: {}. Proceeding with update.", existingTrainer.getId());

        initCredentials(updatedTrainer, existingTrainer);
        logger.debug("Credentials initialized for trainer with ID: {}", existingTrainer.getId());

        try {
            validationService.validateEntity(existingTrainer);
            trainerRepository.save(existingTrainer);
            logger.info("Trainer with ID: {} updated successfully.", existingTrainer.getId());
        } catch (Exception e) {
            logger.error("Failed to update trainer with ID: {}. Error: {}", existingTrainer.getId(), e.getMessage());
        }

        return existingTrainer;
    }


    @Override
    @Authenticate
    public void updateTrainerPassword(Trainer trainer, String newPassword) {
        String id = String.valueOf(trainer.getUser().getId());
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
    public void updateTrainerProfileStatus(Trainer trainer) {
        String id = String.valueOf(trainer.getUser().getId());
        logger.debug("Attempting to deactivate trainer profile for ID: {}", id);
        try {
            boolean statusChanged = userCredentialsService.modifyAccountStatus(id);
            if (statusChanged) {
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
    public Optional<Trainer> getTrainerByUsername(String username, String password) {
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
    public List<Trainer> getAllTrainers() {
        logger.debug("Fetching all trainers");
        List<Trainer> trainers = trainerRepository.findAll();
        logger.info("Found {} trainers", trainers.size());
        return trainers;
    }


    @Override
    @Authenticate
    public List<Training> getTrainerTrainings(Trainer trainer, Date from, Date to, String traineeName) {
        String trainerUsername = trainer.getUser().getUsername();
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
    public List<Trainer> getTrainersNotAssignedToTraineeByUsername(String username, String password) {
        logger.debug("Fetching trainers not assigned to trainee with username: {}", username);
        List<Trainer> trainers = trainerRepository.findTrainersNotAssignedToTraineeByUsername(username);
        logger.info("Found {} trainers not assigned to trainee with username: {}", trainers.size(), username);
        logger.info("Trainers: {}", trainers);
        return trainers;
    }
}

