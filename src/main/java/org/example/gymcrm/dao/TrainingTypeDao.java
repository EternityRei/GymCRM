package org.example.gymcrm.dao;

import org.example.gymcrm.model.TrainingType;

import java.util.List;

public interface TrainingTypeDao {
    TrainingType findById(String id);
    List<TrainingType> findAll();
}
