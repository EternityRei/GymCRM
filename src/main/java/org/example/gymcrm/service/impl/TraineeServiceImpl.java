package org.example.gymcrm.service.impl;

import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.service.TraineeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;

@Service
public class TraineeServiceImpl implements TraineeService {

    private final TraineeDao traineeDao;
    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);

    @Autowired
    public TraineeServiceImpl(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }


    @Override
    public Trainee updateTrainee(String id, Trainee updatedTrainee) {
        logger.info("Updating trainee with ID: {}", id);
        Trainee existingTrainee = traineeDao.findById(id);
        if (existingTrainee != null) {

            existingTrainee.setFirstName(updatedTrainee.getFirstName());
            existingTrainee.setLastName(updatedTrainee.getLastName());
            existingTrainee.setUsername(updatedTrainee.getUsername());
            existingTrainee.setPassword(updatedTrainee.getPassword());
            existingTrainee.setActive(updatedTrainee.isActive());
            existingTrainee.setDateOfBirth(updatedTrainee.getDateOfBirth());
            existingTrainee.setAddress(updatedTrainee.getAddress());

            traineeDao.save(existingTrainee);
            logger.info("Trainee with ID: {} updated successfully", id);
            return existingTrainee;
        } else {
            logger.error("Failed to update trainee. Trainee with ID: {} not found.", id);
            throw new RuntimeException("Trainee with ID " + id + " not found.");
        }
    }


    @Override
    public void deleteTrainee(String id) {
        logger.info("Deleting trainee with ID: {}", id);
        Trainee existingTrainee = traineeDao.findById(id);
        if (existingTrainee != null) {
            traineeDao.deleteById(id);
        } else {
            logger.error("Failed to delete trainee. Trainee with ID: {} not found.", id);
            throw new RuntimeException("Entity was not found");
        }
    }

    @Override
    public Trainee getTrainee(String id) {
        logger.info("Fetching trainee with ID: {}", id);
        return traineeDao.findById(id);
    }

    @Override
    public List<Trainee> getAllTrainees() {
        logger.info("Fetching all trainers");
        return traineeDao.findAll();
    }
}

