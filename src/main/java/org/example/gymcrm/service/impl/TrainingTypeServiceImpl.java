package org.example.gymcrm.service.impl;

import org.example.gymcrm.dao.TrainingTypeDao;
import org.example.gymcrm.model.TrainingType;
import org.example.gymcrm.service.TrainingTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingTypeServiceImpl.class);

    private final TrainingTypeDao trainingTypeDao;

    @Autowired
    public TrainingTypeServiceImpl(TrainingTypeDao trainingTypeDao) {
        this.trainingTypeDao = trainingTypeDao;
    }

    @Override
    public TrainingType findById(String id) {
        logger.info("Fetching training type with ID: {}", id);
        return trainingTypeDao.findById(id);
    }

    @Override
    public List<TrainingType> findAll() {
        logger.info("Fetching all training types.");
        return trainingTypeDao.findAll();
    }
}
