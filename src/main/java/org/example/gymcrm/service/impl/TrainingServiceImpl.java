package org.example.gymcrm.service.impl;

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
        logger.debug("Attempting to create training");

        logger.debug("Validating the training entity");
        validationService.validateEntity(training);
        logger.debug("Saving the training entity: {}", training.toString());

        trainingRepository.save(training);
        logger.info("Training created successfully with ID: {}", training.getTrainingId());
        return training;
    }

    @Override
    public Optional<Training> getTraining(String id) {
        logger.debug("Attempting to fetch training with ID: {}", id);

        Optional<Training> training = trainingRepository.findById(Long.valueOf(id));
        if (training.isPresent()) {
            logger.info("Training found with ID: {}", id);
        } else {
            logger.warn("No training found with ID: {}", id);
        }

        return training;
    }


    @Override
    public List<Training> getAllTrainings() {
        logger.debug("Fetching all trainings.");

        List<Training> trainings = trainingRepository.findAll();
        if (trainings.isEmpty()) {
            logger.info("No trainings found.");
        } else {
            logger.info("Found {} trainings.", trainings.size());
        }

        return trainings;
    }

    @Override
    public List<Training> findTrainings(String traineeUsername, String trainerUsername, Date start, Date end, String trainerName, String traineeName, String trainingType) {
        logger.debug("Attempting to find trainings with criteria - TraineeUsername: {}, " +
                        "TrainerUsername: {}, " +
                        "StartDate: {}," +
                        " EndDate: {}," +
                        " TrainerName: {}," +
                        " TraineeName: {}, " +
                        "TrainingType: {}",
                traineeUsername,
                trainerUsername,
                start,
                end,
                trainerName,
                traineeName,
                trainingType);

        Specification<Training> spec = TrainingSpecification
                .byCriteria(traineeUsername,
                        trainerUsername,
                        start,
                        end,
                        trainerName,
                        traineeName,
                        trainingType);

        List<Training> trainings = trainingRepository.findAll(spec);

        if (trainings.isEmpty()) {
            logger.info("No trainings found matching the specified criteria.");
        } else {
            logger.info("Found {} trainings matching the specified criteria.", trainings.size());
        }

        return trainings;
    }

}

