package org.example.gymcrm.dao;

import org.example.gymcrm.model.Training;

import java.util.List;

public interface TrainingDao {
    Training save(Training training);
    Training findById(String id);
    List<Training> findAll();
    void deleteById(String id);
}
