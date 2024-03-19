package org.example.gymcrm.repository;

import org.example.gymcrm.model.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    @Query("SELECT t FROM Trainee t JOIN t.user u WHERE u.username = :username")
    Optional<Trainee> findByUsername(@Param("username") String username);
}
