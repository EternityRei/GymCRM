package org.example.gymcrm.dao;

import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.model.Trainer;

import java.util.List;

public interface TrainerDao {
    Trainer save(Trainer trainer);
    Trainer findById(String id);
    List<Trainer> findAll();
    void deleteById(String id);
    List<Trainer> findByFirstNameAndLastNameStartingWith(String firstName, String lastNamePrefix);
}
