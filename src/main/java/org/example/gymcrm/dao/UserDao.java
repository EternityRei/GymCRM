package org.example.gymcrm.dao;

import org.example.gymcrm.model.User;

import java.util.List;

public interface UserDao {
    User save(User user);
    List<User> findUsers(User user);
    void deleteById(User user);
}
