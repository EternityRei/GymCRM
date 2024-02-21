package org.example.gymcrm.service.impl;

import org.example.gymcrm.dao.TrainingDao;
import org.example.gymcrm.model.Training;
import org.example.gymcrm.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private final TrainingDao trainingDao;

    @Autowired
    public TrainingServiceImpl(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    @Override
    public Training createTraining(Training training) {
        logger.info("Attempting to create training with ID: {}", training.getTrainingId());
        Training existingTraining = trainingDao.findById(training.getTrainingId());

        if (existingTraining == null) {
            trainingDao.save(training);
            logger.info("Training with ID: {} created successfully.", training.getTrainingId());
            return training;
        } else {
            logger.warn("Creation failed: A training with ID: {} already exists.", training.getTrainingId());
            throw new IllegalStateException("A training with ID " + training.getTrainingId() + " already exists.");
        }
    }

    @Override
    public Training getTraining(String id) {
        logger.info("Fetching training with ID: {}", id);
        return trainingDao.findById(id);
    }

    @Override
    public List<Training> getAllTrainings() {
        logger.info("Fetching all trainings.");
        return trainingDao.findAll();
    }
}

