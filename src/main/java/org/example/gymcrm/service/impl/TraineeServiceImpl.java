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
import java.util.HashSet;
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
    public void updateTraineePassword(Trainee trainee, String newPassword) {
        String id = String.valueOf(trainee.getId());
        logger.debug("Attempting to update password for trainee with ID: {}", id);

        try {
            Trainee foundTrainee = traineeRepository.findById(Long.valueOf(id))
                    .orElseThrow(() -> new RuntimeException("Trainee not found"));
            String userId = String.valueOf(foundTrainee.getUser().getId());
            userCredentialsGenerator.updatePassword(userId, newPassword);
            logger.info("Password updated successfully for trainee with ID: {}", id);
        } catch (Exception e) {
            logger.error("Failed to update password for trainee with ID: {}", id, e);
        }
    }


    @Override
    @Authenticate
    public void updateTraineeProfileStatus(Trainee trainee) {
        String id = String.valueOf(trainee.getUser().getId());
        logger.debug("Attempting to change trainee profile status with userId: {}", id);

        try {
            userCredentialsGenerator.modifyAccountStatus(id);
            logger.info("Trainee profile status was changed successfully for userId: {}", id);
        } catch (Exception e) {
            logger.error("Failed to change trainee profile status for userId: {}", id, e);
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
    public void deleteTraineeByUsername(String username, String password) {
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
    public Trainee getTraineeByUsername(String username, String password) {
        logger.debug("Attempting to fetch trainee by username: {}", username);
        Trainee trainee = null;
        try{
            trainee = traineeRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
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
    public List<Training> getTraineeTrainings(Trainee trainee, Date from, Date to, String trainerName, String trainingType) {
        String traineeUsername = trainee.getUser().getUsername();
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
    public void addTrainersToTrainee(Trainee trainee, List<Trainer> newTrainers) {
        String traineeUsername = trainee.getUser().getUsername();
        logger.debug("Attempting to add trainers to trainee with username={}", traineeUsername);

        Set<Trainer> existingTrainers = getExistingTrainers(trainee);

        // Assuming updateTrainersList has been adjusted to accept List<Trainer> for the new trainers
        logger.debug("Current trainers for traineeUsername={}: {}. Adding new trainers.", traineeUsername, existingTrainers.stream().map(Trainer::getId).collect(Collectors.toList()));

        updateTrainersList(newTrainers, existingTrainers, trainee); // Corrected to pass newTrainers directly

        try {
            traineeRepository.save(trainee);
            logger.info("Successfully added trainers to trainee with username={}. Updated trainers: {}", traineeUsername, getExistingTrainers(trainee).stream().map(Trainer::getId).collect(Collectors.toList()));
        } catch (Exception e) {
            logger.error("Failed to add trainers to trainee with username={}", traineeUsername, e);
            throw e;
        }
    }

    private void updateTrainersList(List<Trainer> newTrainers, Set<Trainer> existingTrainers, Trainee trainee) {
        logger.debug("Starting to update trainers list for trainee with ID: {}. New trainers being added.", trainee.getId());

        // Log the IDs of new trainers for debugging purposes
        logger.debug("New trainer IDs: {}", newTrainers.stream().map(Trainer::getId).collect(Collectors.toList()));

        // Use Set of existing trainer IDs for comparison to avoid adding duplicates
        Set<Long> existingTrainerIds = existingTrainers.stream().map(Trainer::getId).collect(Collectors.toSet());

        newTrainers.forEach(trainer -> {
            // Check if the trainer is already associated with the trainee and exists in the system
            if (!existingTrainerIds.contains(trainer.getId())) {
                try {
                    // Verify the trainer exists in the system before adding
                    Trainer existingTrainer = trainerRepository.findById(trainer.getId())
                            .orElseThrow(() -> new RuntimeException("Trainer not found in the system: " + trainer.getId()));
                    // Since the trainer exists, add it to the trainee
                    trainee.getTrainers().add(existingTrainer);
                    logger.info("Added trainer with ID: {} to trainee with ID: {}", trainer.getId(), trainee.getId());
                } catch (RuntimeException e) {
                    logger.error("Unable to add trainer with ID: {} to trainee with ID: {}. Error: {}", trainer.getId(), trainee.getId(), e.getMessage());
                    // Depending on how you want to handle errors, either log and continue or rethrow the exception
                    throw e;
                }
            }
        });

        logger.debug("Completed updating trainers list for trainee with ID: {}", trainee.getId());
    }



    private Set<Trainer> getExistingTrainers(Trainee trainee) {
        Set<Trainer> existingTrainers = new HashSet<>();
        try {
            for (Trainer trainer : trainee.getTrainers()) {
                trainerRepository.findById(trainer.getId()).ifPresent(existingTrainers::add);
            }
            logger.debug("Retrieved {} existing trainers for trainee with ID: {}", existingTrainers.size(), trainee.getId());
        } catch (Exception e) {
            logger.error("Error retrieving existing trainers for trainee with ID: {}", trainee.getId(), e);
        }
        return existingTrainers;
    }

}

