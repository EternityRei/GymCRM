package org.example.gymcrm.repository;

import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dao.UserDao;
import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    private final TrainerDao trainerDao;
    private final TraineeDao traineeDao;

    @Autowired
    public UserDaoImpl(TrainerDao trainerDao, TraineeDao traineeDao) {
        this.trainerDao = trainerDao;
        this.traineeDao = traineeDao;
    }

    @Override
    public User save(User user) {
        if (user instanceof Trainer) {
            return trainerDao.save((Trainer) user);
        } else if (user instanceof Trainee) {
            return traineeDao.save((Trainee) user);
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }
    }
    @Override
    public List<User> findUsers(User user) {
        if (user instanceof Trainer) {
            trainerDao.findByFirstNameAndLastNameStartingWith(user.getFirstName(), user.getLastName());
        } else if (user instanceof Trainee) {
            traineeDao.findByFirstNameAndLastNameStartingWith(user.getFirstName(), user.getLastName());
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }
        return null;
    }

    @Override
    public void deleteById(User user) {
        if (user instanceof Trainer) {
            trainerDao.deleteById(user.getId());
        } else if (user instanceof Trainee) {
            traineeDao.deleteById(user.getId());
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }
    }
}
