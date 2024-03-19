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
        logger.info("Fetching training type with ID: {}", id);
        return trainingTypeRepository.findById(Long.valueOf(id));
    }

    @Override
    public List<TrainingType> findAll() {
        logger.info("Fetching all training types.");
        return trainingTypeRepository.findAll();
    }
}
