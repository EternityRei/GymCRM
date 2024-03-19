package org.example.gymcrm.specification;

import org.example.gymcrm.model.Training;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrainingSpecification {

    public static Specification<Training> withTraineeUsername(String username) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join("trainee").join("user").get("username"), username);
    }
    public static Specification<Training> withTrainerUsername(String username) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join("trainer").join("user").get("username"), username);
    }
    public static Specification<Training> withDateRange(Date start, Date end) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("trainingDate"), start, end);
    }

    public static Specification<Training> withTrainerName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join("trainer").join("user").get("firstName"), name);
    }
    public static Specification<Training> withTraineeName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join("trainee").join("user").get("firstName"), name);
    }

    public static Specification<Training> withTrainingType(String trainingType) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("trainingType").get("name"), trainingType);
    }

    public static Specification<Training> byCriteria(String traineeUsername, String trainerUsername, Date start, Date end, String trainerName, String traineeName, String trainingType) {
        List<Specification<Training>> specifications = new ArrayList<>();
        if (traineeUsername != null && !traineeUsername.isEmpty()) {
            specifications.add(withTraineeUsername(traineeUsername));
        }
        if (trainerUsername != null && !trainerUsername.isEmpty()) {
            specifications.add(withTrainerUsername(trainerUsername));
        }
        if (start != null && end != null) {
            specifications.add(withDateRange(start, end));
        }
        if (trainerName != null && !trainerName.isEmpty()) {
            specifications.add(withTrainerName(trainerName));
        }
        if (traineeName != null && !traineeName.isEmpty()) {
            specifications.add(withTraineeName(traineeName));
        }
        if (trainingType != null && !trainingType.isEmpty()) {
            specifications.add(withTrainingType(trainingType));
        }
        return specifications.stream().reduce(Specification::and).orElse(null);
    }


}
