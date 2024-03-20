package org.example.gymcrm.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.gymcrm.annotation.Authenticate;
import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.Training;
import org.example.gymcrm.model.User;
import org.example.gymcrm.repository.TraineeRepository;
import org.example.gymcrm.repository.TrainerRepository;
import org.example.gymcrm.repository.TrainingRepository;
import org.example.gymcrm.service.TraineeService;
import org.example.gymcrm.service.ValidationService;
import org.example.gymcrm.specification.TrainingSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TraineeServiceImpl implements TraineeService {
    private final UserServiceImpl userCredentialsGenerator;
    private final TraineeRepository traineeRepository;
    private final TrainingRepository trainingRepository;
    private final TrainerRepository trainerRepository;
    private final ValidationService validationService;
    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);

    @Autowired
    public TraineeServiceImpl(UserServiceImpl userCredentialsGenerator, TraineeRepository traineeRepository, TrainingRepository trainingRepository, TrainerRepository trainerRepository, ValidationService validationService) {
        this.userCredentialsGenerator = userCredentialsGenerator;
        this.traineeRepository = traineeRepository;
        this.trainingRepository = trainingRepository;
        this.trainerRepository = trainerRepository;
        this.validationService = validationService;
    }


    @Override
    public Trainee createTrainee(Trainee trainee) {
        User user = trainee.getUser();

        logger.debug("Generating credentials for new trainee - " +
                "First Name: {}," +
                " Last Name: {}",
                user.getFirstName(),
                user.getLastName());

        String username = userCredentialsGenerator.createUsername(user.getFirstName(), user.getLastName());
        String password = userCredentialsGenerator.generateRandomPassword();

        user.setUsername(username);
        user.setPassword(password);

        logger.debug("Validating generated user credentials");
        validationService.validateEntity(user);

        trainee.setUser(user);

        logger.info("Trainee created with username: {}", username);
        return traineeRepository.save(trainee);
    }

    @Override
    @Authenticate
    public Trainee updateTrainee(Trainee updatedTrainee) {
        logger.debug("Attempting to update trainee with ID: {}", updatedTrainee.getId());

        Trainee existingTrainee;
        try {
            existingTrainee = traineeRepository.findById(updatedTrainee.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Trainee not found with ID: " + updatedTrainee.getId()));
        } catch (EntityNotFoundException e) {
            logger.error("Failed to find trainee with ID: {}", updatedTrainee.getId(), e);
            throw e;
        }

        logger.debug("Initializing credentials for trainee with ID: {}", existingTrainee.getId());
        try {
            initCredentials(updatedTrainee, existingTrainee);
        } catch (Exception e) {
            logger.error("Error initializing credentials for trainee with ID: {}", existingTrainee.getId(), e);
            throw e;
        }

        logger.debug("Validating updated trainee entity for ID: {}", existingTrainee.getId());
        try {
            validationService.validateEntity(existingTrainee);
        } catch (Exception e) {
            logger.error("Validation failed for updated trainee with ID: {}", existingTrainee.getId(), e);
            throw e;
        }

        traineeRepository.save(existingTrainee);
        logger.info("Trainee with ID: {} updated successfully", existingTrainee.getId());
        return existingTrainee;
    }

    @Override
    @Authenticate
    public void updateTraineePassword(String id, String newPassword) {
        logger.debug("Attempting to update password for trainee with ID: {}", id);

        try {
            userCredentialsGenerator.updatePassword(id, newPassword);
            logger.info("Password updated successfully for trainee with ID: {}", id);
        } catch (Exception e) {
            logger.error("Failed to update password for trainee with ID: {}", id, e);
        }
    }


    @Override
    @Authenticate
    public void updateTraineeProfileStatus(String id) {
        logger.debug("Attempting to change trainee profile status with ID: {}", id);

        try {
            userCredentialsGenerator.modifyAccountStatus(id);
            logger.info("Trainee profile status was changed successfully for ID: {}", id);
        } catch (Exception e) {
            logger.error("Failed to change trainee profile status for ID: {}", id, e);
        }
    }


    private void initCredentials(Trainee updatedTrainee, Trainee existingTrainee) {
        existingTrainee.getUser().setFirstName(updatedTrainee.getUser().getFirstName());
        existingTrainee.getUser().setLastName(updatedTrainee.getUser().getLastName());
        existingTrainee.getUser().setActive(updatedTrainee.getUser().isActive());
        existingTrainee.setDateOfBirth(updatedTrainee.getDateOfBirth());
        existingTrainee.setAddress(updatedTrainee.getAddress());
    }

    @Override
    @Authenticate
    public void deleteTrainee(String id) {
        logger.debug("Attempting to delete trainee with ID: {}", id);

        try {
            traineeRepository.deleteById(Long.valueOf(id));
            logger.info("Trainee successfully deleted with ID: {}", id);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("No trainee found with ID: {} to delete", id);
        } catch (Exception e) {
            logger.error("Error deleting trainee with ID: {}", id, e);
        }
    }


    @Override
    @Authenticate
    public void deleteTraineeByUsername(String username) {
        logger.debug("Attempting to delete trainee with username: {}", username);

        try {
            Trainee trainee = traineeRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("Trainee not found with username: " + username));
            traineeRepository.delete(trainee);
            logger.info("Trainee successfully deleted with username: {}", username);
        } catch (EntityNotFoundException e) {
            logger.warn("No trainee found with username: {}", username, e);
        } catch (Exception e) {
            logger.error("Error deleting trainee with username: {}", username, e);
        }
    }


    @Override
    @Authenticate
    public Optional<Trainee> getTrainee(String id) {
        logger.debug("Attempting to fetch trainee with ID: {}", id);
        Optional<Trainee> trainee = traineeRepository.findById(Long.valueOf(id));
        if (trainee.isPresent()) {
            logger.info("Trainee found with ID: {}", id);
        } else {
            logger.warn("No trainee found with ID: {}", id);
        }
        return trainee;
    }

    @Override
    @Authenticate
    public Trainee getTraineeByUsername(String username) {
        logger.debug("Attempting to fetch trainee by username: {}", username);
        try{
            Trainee trainee = traineeRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
            logger.info("Trainee found with username: {}", username);
            return trainee;
        } catch (EntityNotFoundException e){
            logger.warn("No trainee found with username: {}", username);
            throw e;
        }
    }

    @Override
    @Authenticate
    public List<Trainee> getAllTrainees() {
        logger.debug("Fetching all trainees");
        List<Trainee> trainees = traineeRepository.findAll();
        if (trainees.isEmpty()) {
            logger.info("No trainees found");
        } else {
            logger.info("Found {} trainees", trainees.size());
        }
        return trainees;
    }

    @Override
    @Authenticate
    public List<Training> getTraineeTrainings(String traineeUsername, Date from, Date to, String trainerName, String trainingType) {
        logger.info("Fetching trainings for " +
                        "traineeUsername={}," +
                        " from={}, " +
                        "to={}, " +
                        "trainerName={}," +
                        " trainingType={}",
                traineeUsername,
                from,
                to,
                trainerName,
                trainingType);

        Specification<Training> spec = TrainingSpecification
                .byCriteria(
                        traineeUsername,
                        null,
                        from,
                        to,
                        trainerName,
                        null,
                        trainingType
                );

        List<Training> trainings = trainingRepository.findAll(spec);

        if (trainings.isEmpty()) {
            logger.warn("No trainings found matching the specified criteria for traineeUsername={}", traineeUsername);
        } else {
            logger.info("Found {} trainings for traineeUsername={}", trainings.size(), traineeUsername);
        }

        return trainings;
    }


    @Transactional
    @Authenticate
    public void addTrainersToTrainee(String traineeUsername, List<Long> newTrainerIds) {
        logger.debug("Attempting to add trainers to trainee with username={}", traineeUsername);
        Trainee trainee;
        try {
            trainee = traineeRepository.findByUsername(traineeUsername)
                    .orElseThrow(() -> new RuntimeException("Trainee not found"));
        } catch (RuntimeException e) {
            logger.error("Failed to find trainee with username={}", traineeUsername, e);
            throw e;
        }

        Set<Long> existingTrainerIds = getExistingTrainerIds(trainee);

        logger.debug("Current trainer IDs for traineeUsername={}: {}. Adding new trainer IDs: {}", traineeUsername, existingTrainerIds, newTrainerIds);

        updateTrainersList(newTrainerIds, existingTrainerIds, trainee);

        try {
            traineeRepository.save(trainee);
            logger.info("Successfully added trainers to trainee with username={}. Updated trainer IDs: {}", traineeUsername, getExistingTrainerIds(trainee));
        } catch (Exception e) {
            logger.error("Failed to add trainers to trainee with username={}", traineeUsername, e);
            throw e;
        }
    }


    private void updateTrainersList(List<Long> newTrainerIds, Set<Long> existingTrainerIds, Trainee trainee) {
        logger.debug("Starting to update trainers list for trainee with ID: {}. New trainer IDs: {}", trainee.getId(), newTrainerIds);

        newTrainerIds.stream()
                .filter(id -> !existingTrainerIds.contains(id)) // Filter out already associated trainers
                .forEach(id -> {
                    try {
                        Trainer trainer = trainerRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Trainer not found: " + id));
                        trainee.getTrainers().add(trainer); // Add new trainers
                        logger.info("Added trainer with ID: {} to trainee with ID: {}", id, trainee.getId());
                    } catch (RuntimeException e) {
                        logger.error("Unable to find or add trainer with ID: {} to trainee with ID: {}", id, trainee.getId(), e);
                        throw e;
                    }
                });

        logger.debug("Completed updating trainers list for trainee with ID: {}", trainee.getId());
    }

    private Set<Long> getExistingTrainerIds(Trainee trainee) {
        Set<Long> trainerIds;
        try {
            trainerIds = trainee.getTrainers().stream()
                    .map(Trainer::getId)
                    .collect(Collectors.toSet());

            logger.debug("Retrieved {} existing trainer IDs for trainee with ID: {}", trainerIds.size(), trainee.getId());
        } catch (Exception e) {
            logger.error("Error retrieving existing trainer IDs for trainee with ID: {}", trainee.getId(), e);
            throw e;
        }
        return trainerIds;
    }

    @Transactional
    public Optional<Trainee> getTraineeByUsernameAuthentication(String username) {
        return traineeRepository.findByUsername(username);
    }

    public Optional<Trainee> getTraineeAuthentication(String id) {
        return traineeRepository.findById(Long.valueOf(id));
    }
}

