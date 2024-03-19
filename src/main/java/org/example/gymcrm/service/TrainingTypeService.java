package org.example.gymcrm.service;

import org.example.gymcrm.model.TrainingType;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeService {
    Optional<TrainingType> findById(String id);
    List<TrainingType> findAll();
}
