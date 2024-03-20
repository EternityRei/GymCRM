package org.example.gymcrm.service.impl;

import org.example.gymcrm.annotation.Authenticate;
import org.example.gymcrm.model.TrainingType;
import org.example.gymcrm.repository.TrainingTypeRepository;
import org.example.gymcrm.service.TrainingTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Authenticate
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingTypeServiceImpl.class);

    private final TrainingTypeRepository trainingTypeRepository;

    @Autowired
    public TrainingTypeServiceImpl(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    public Optional<TrainingType> findById(String id) {
        logger.debug("Attempting to fetch training type with ID: {}", id);
        Optional<TrainingType> trainingType = trainingTypeRepository.findById(Long.valueOf(id));
        if (trainingType.isPresent()) {
            logger.info("Training type found with ID: {}", id);
        } else {
            logger.warn("No training type found with ID: {}", id);
        }
        return trainingType;
    }

    @Override
    public List<TrainingType> findAll() {
        logger.debug("Fetching all training types.");
        List<TrainingType> trainingTypes = trainingTypeRepository.findAll();
        if (trainingTypes.isEmpty()) {
            logger.info("No training types found.");
        } else {
            logger.info("Found {} training types", trainingTypes.size());
        }
        return trainingTypes;
    }

    @Override
    public TrainingType createTrainingType(String name) {
        TrainingType trainingType = new TrainingType();
        trainingType.setName(name);
        return trainingTypeRepository.save(trainingType);
    }
}
