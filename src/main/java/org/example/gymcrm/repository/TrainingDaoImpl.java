package org.example.gymcrm.repository;

import org.example.gymcrm.dao.TrainingDao;
import org.example.gymcrm.model.Training;
import org.example.gymcrm.storage.TrainingStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrainingDaoImpl implements TrainingDao {

    private TrainingStorage trainingStorage;

    public TrainingDaoImpl() {
    }

    @Autowired
    public void setTrainingStorage(TrainingStorage trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    @Override
    public Training save(Training training) {
        return trainingStorage.save(training);
    }

    @Override
    public Training findById(String id) {
        return trainingStorage.findById(id);
    }

    @Override
    public List<Training> findAll() {
        return trainingStorage.findAll();
    }

    @Override
    public void deleteById(String id) {
        trainingStorage.deleteById(id);
    }
}
