package org.example.gymcrm.model;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;

public class Training {
    private String trainingId;
    private Trainer trainer;
    private Trainee trainee;
    private TrainingType trainingType;
    private String trainingName;
    private Date trainingDate;
    private long trainingDuration;

    public Training(String trainingId, Trainer trainer, Trainee trainee, TrainingType trainingType, String trainingName, Date trainingDate, long trainingDuration) {
        this.trainingId = trainingId;
        this.trainer = trainer;
        this.trainee = trainee;
        this.trainingType = trainingType;
        this.trainingName = trainingName;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public Training() {

    }

    public String getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(String trainingId) {
        this.trainingId = trainingId;
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

    public void setTrainingDuration(long trainingDuration) {
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
                "trainingId='" + trainingId + '\'' +
                ", trainer=" + trainer +
                ", trainee=" + trainee +
                ", trainingType=" + trainingType +
                ", trainingName='" + trainingName + '\'' +
                ", trainingDate=" + trainingDate +
                ", trainingDuration=" + trainingDuration +
                '}';
    }
}
