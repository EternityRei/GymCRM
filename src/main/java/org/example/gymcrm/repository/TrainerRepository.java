package org.example.gymcrm.repository;


import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    @Query("SELECT t FROM Trainer t JOIN t.user u WHERE u.username = :username")
    Optional<Trainer> findByUsername(@Param("username") String username);

    @Query("SELECT t FROM Trainer t WHERE t NOT IN (SELECT tr.trainer FROM Training tr WHERE tr.trainee.user.username = :username)")
    List<Trainer> findTrainersNotAssignedToTraineeByUsername(@Param("username") String username);
}
