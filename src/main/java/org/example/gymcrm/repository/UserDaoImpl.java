package org.example.gymcrm.repository;

import org.example.gymcrm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDaoImpl extends JpaRepository<User, Long> {
}
