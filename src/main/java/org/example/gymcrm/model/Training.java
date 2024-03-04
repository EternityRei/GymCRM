package org.example.gymcrm.model;

import jakarta.persistence.*;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "training")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trainee_id", referencedColumnName = "id")
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id", referencedColumnName = "id")
    private Trainer trainer;

    @Column(name = "training_name", nullable = false)
    private String trainingName;

    @ManyToOne
    @JoinColumn(name = "training_type_id", referencedColumnName = "id")
    private TrainingType trainingType;

    @Column(name = "training_date", nullable = false)
    private Date trainingDate;

    @Column(name = "training_duration", nullable = false)
    private Integer trainingDuration;

    public Training(Long id, Trainer trainer, Trainee trainee, TrainingType trainingType, String trainingName, Date trainingDate, Integer trainingDuration) {
        this.id = id;
        this.trainer = trainer;
        this.trainee = trainee;
        this.trainingType = trainingType;
        this.trainingName = trainingName;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public Training() {

    }

    public Long getTrainingId() {
        return id;
    }

    public void setTrainingId(Long trainingId) {
        this.id = trainingId;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public Date getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }

    public long getTrainingDuration() {
        return trainingDuration;
    }

    public void setTrainingDuration(Integer trainingDuration) {
        this.trainingDuration = trainingDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Training training)) return false;
        return Objects.equals(getTrainingId(), training.getTrainingId()) && Objects.equals(getTrainer(), training.getTrainer()) && Objects.equals(getTrainee(), training.getTrainee()) && getTrainingType() == training.getTrainingType() && Objects.equals(getTrainingName(), training.getTrainingName()) && Objects.equals(getTrainingDate(), training.getTrainingDate()) && Objects.equals(getTrainingDuration(), training.getTrainingDuration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTrainingId(), getTrainer(), getTrainee(), getTrainingType(), getTrainingName(), getTrainingDate(), getTrainingDuration());
    }

    @Override
    public String toString() {
        return "Training{" +
                "id='" + id + '\'' +
                ", trainer=" + trainer +
                ", trainee=" + trainee +
                ", trainingType=" + trainingType +
                ", trainingName='" + trainingName + '\'' +
                ", trainingDate=" + trainingDate +
                ", trainingDuration=" + trainingDuration +
                '}';
    }
}
