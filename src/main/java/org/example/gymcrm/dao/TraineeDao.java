package org.example.gymcrm.dao;

import org.example.gymcrm.model.Trainee;

import java.util.List;

public interface TraineeDao {
    Trainee save(Trainee trainee);
    Trainee findById(String id);
    List<Trainee> findAll();
    void deleteById(String id);
    List<Trainee> findByFirstNameAndLastNameStartingWith(String firstName, String lastNamePrefix);
}
