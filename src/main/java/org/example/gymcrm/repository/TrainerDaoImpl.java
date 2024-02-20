package org.example.gymcrm.repository;


import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.storage.TrainerStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class TrainerDaoImpl implements TrainerDao {

    private TrainerStorage trainerStorage;

    public TrainerDaoImpl() {
    }

    @Autowired
    public void setTrainerStorage(TrainerStorage trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    @Override
    public Trainer save(Trainer trainer) {
        return trainerStorage.save(trainer);
    }

    @Override
    public Trainer findById(String id) {
        return trainerStorage.findById(id);
    }

    @Override
    public List<Trainer> findAll() {
        return trainerStorage.findAll();
    }

    @Override
    public void deleteById(String id) {
        trainerStorage.deleteById(id);
    }

    @Override
    public List<Trainer> findByFirstNameAndLastNameStartingWith(String firstName, String lastNamePrefix) {
        return trainerStorage.findByFirstNameAndLastNameStartingWith(firstName, lastNamePrefix);
    }
}
