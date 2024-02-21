package org.example.gymcrm.repository;

import org.example.gymcrm.dao.TrainingTypeDao;
import org.example.gymcrm.model.TrainingType;
import org.example.gymcrm.storage.TrainingStorage;
import org.example.gymcrm.storage.TrainingTypeStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrainingTypeDaoImpl implements TrainingTypeDao {

    private TrainingTypeStorage trainingTypeStorage;

    public TrainingTypeDaoImpl() {
    }

    @Autowired
    public void setTrainingStorage(TrainingTypeStorage trainingTypeStorage) {
        this.trainingTypeStorage = trainingTypeStorage;
    }

    @Override
    public TrainingType findById(String id) {
        return trainingTypeStorage.findById(id);
    }

    @Override
    public List<TrainingType> findAll() {
        return trainingTypeStorage.findAll();
    }
}
