package org.example.gymcrm.service.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.example.gymcrm.annotation.Authenticate;
import org.example.gymcrm.model.Training;
import org.example.gymcrm.repository.TrainingRepository;
import org.example.gymcrm.service.TrainingService;
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
import java.util.Set;

@Service
@Authenticate
public class TrainingServiceImpl implements TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private final TrainingRepository trainingRepository;

    private final ValidationService validationService;

    @Autowired
    public TrainingServiceImpl(TrainingRepository trainingRepository, ValidationService validationService) {
        this.trainingRepository = trainingRepository;
        this.validationService = validationService;
    }

    @Override
    public Training createTraining(Training training) {
        logger.info("Attempting to create training");

        // Validate the training entity
        validationService.validateEntity(training);

        // Assuming ID is generated, no need to check if it already exists as in the original method
        trainingRepository.save(training);
        logger.info("Training created successfully.");
        return training;
    }

    @Override
    public Optional<Training> getTraining(String id) {
        logger.info("Fetching training with ID: {}", id);
        return trainingRepository.findById(Long.valueOf(id));
    }

    @Override
    public List<Training> getAllTrainings() {
        logger.info("Fetching all trainings.");
        return trainingRepository.findAll();
    }

    @Override
    public List<Training> findTrainings(String traineeUsername, String trainerUsername, Date start, Date end, String trainerName, String traineeName, String trainingType) {
        Specification<Training> spec = TrainingSpecification.byCriteria(traineeUsername, trainerUsername, start, end, trainerName, traineeName, trainingType);
        return trainingRepository.findAll(spec);
    }
}

