package org.example.gymcrm.repository;

import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.storage.TraineeStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class TraineeDaoImpl implements TraineeDao {

    private TraineeStorage traineeStorage;

    public TraineeDaoImpl() {
    }

    @Autowired
    public void setTraineeStorage(TraineeStorage traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    @Override
    public Trainee save(Trainee trainee) {
        return traineeStorage.save(trainee);
    }

    @Override
    public Trainee findById(String id) {
        return traineeStorage.findById(id);
    }

    @Override
    public List<Trainee> findAll() {
        return traineeStorage.findAll();
    }

    @Override
    public void deleteById(String id) {
        traineeStorage.deleteById(id);
    }

    @Override
    public List<Trainee> findByFirstNameAndLastNameStartingWith(String firstName, String lastNamePrefix) {
        return traineeStorage.findByFirstNameAndLastNameStartingWith(firstName, lastNamePrefix);
    }
}
