package org.example.gymcrm.service;

import org.example.gymcrm.model.TrainingType;

import java.util.List;

public interface TrainingTypeService {
    TrainingType findById(String id);
    List<TrainingType> findAll();
}
