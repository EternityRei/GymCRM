package org.example.gymcrm.repository;

import org.example.gymcrm.model.Training;
import org.example.gymcrm.model.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long>, JpaSpecificationExecutor<Training> {
}
