package org.example.gymcrm.service.impl;

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
        String username = userCredentialsGenerator.createUsername(user.getFirstName(), user.getLastName());
        String password = userCredentialsGenerator.generateRandomPassword();

        user.setUsername(username);
        user.setPassword(password);

        validationService.validateEntity(user);

        trainee.setUser(user);

        logger.info("Trainee created with username: {}", username);
        return traineeRepository.save(trainee);
    }

    @Override
    @Authenticate
    public Trainee updateTrainee(String id, Trainee updatedTrainee) {
        logger.info("Updating trainee with ID: {}", id);
        Trainee existingTrainee = traineeRepository.findById(Long.valueOf(id)).orElseThrow();
        initCredentials(updatedTrainee, existingTrainee);

        validationService.validateEntity(existingTrainee);

        traineeRepository.save(existingTrainee);
        logger.info("Trainee with ID: {} updated successfully", id);
        return existingTrainee;
    }

    @Override
    @Authenticate
    public void updateTraineePassword(String id, String password) {
        logger.info("Updating trainee password");
        userCredentialsGenerator.updatePassword(id, password);
    }

    @Override
    @Authenticate
    public void deactivateTraineeProfile(String id) {
        logger.info("Changing trainee account status");
        userCredentialsGenerator.banUser(id);
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
        logger.info("Deleting trainee with ID: {}", id);
        traineeRepository.deleteById(Long.valueOf(id));
    }

    @Override
    @Authenticate
    public void deleteTraineeByUsername(String username) {
        logger.info("Deleting trainee with username={}", username);
        Trainee trainee = traineeRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Trainee not found"));
        traineeRepository.delete(trainee);
    }

    @Override
    @Authenticate
    public Optional<Trainee> getTrainee(String id) {
        logger.info("Fetching trainee with ID: {}", id);
        return traineeRepository.findById(Long.valueOf(id));
    }

    @Override
    @Authenticate
    public Optional<Trainee> getTraineeByUsername(String username) {
        logger.info("Fetching trainee by username={}", username);
        return traineeRepository.findByUsername(username);
    }

    @Override
    @Authenticate
    public List<Trainee> getAllTrainees() {
        logger.info("Fetching all trainers");
        return traineeRepository.findAll();
    }

    @Override
    @Authenticate
    public List<Training> getTraineeTrainings(String traineeUsername, Date from, Date to, String trainerName, String trainingType) {
        Specification<Training> spec = TrainingSpecification.byCriteria(traineeUsername, null, from, to, trainerName, null, trainingType);
        return trainingRepository.findAll(spec);
    }

    @Transactional
    @Authenticate
    public void addTrainersToTrainee(String traineeUsername, List<Long> newTrainerIds) {
        Trainee trainee = traineeRepository.findByUsername(traineeUsername)
                .orElseThrow(() -> new RuntimeException("Trainee not found"));

        Set<Long> existingTrainerIds = getExistingTrainerIds(trainee);

        updateTrainersList(newTrainerIds, existingTrainerIds, trainee);

        traineeRepository.save(trainee);
    }

    private void updateTrainersList(List<Long> newTrainerIds, Set<Long> existingTrainerIds, Trainee trainee) {
        newTrainerIds.stream()
                .filter(id -> !existingTrainerIds.contains(id)) // Filter out already associated trainers
                .forEach(id -> {
                    Trainer trainer = trainerRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Trainer not found: " + id));
                    trainee.getTrainers().add(trainer); // Add new trainers
                });
    }

    private Set<Long> getExistingTrainerIds(Trainee trainee) {
        return trainee.getTrainers().stream()
                .map(Trainer::getId)
                .collect(Collectors.toSet());
    }
}

