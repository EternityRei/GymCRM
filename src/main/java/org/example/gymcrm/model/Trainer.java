package org.example.gymcrm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.gymcrm.model.group.OnCreate;
import org.example.gymcrm.model.group.OnUpdate;

import java.util.Objects;

@Entity
@Table(name = "trainers")
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @JoinColumn(name = "specialization", referencedColumnName = "id")
    private TrainingType specialization;

    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Trainer(Long id, TrainingType specialization, User user) {
        this.id = id;
        this.specialization = specialization;
        this.user = user;
    }

    public Trainer(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trainer trainer)) return false;
        return Objects.equals(getId(), trainer.getId()) && Objects.equals(getSpecialization(), trainer.getSpecialization()) && Objects.equals(getUser(), trainer.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSpecialization(), getUser());
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "id=" + id +
                ", specialization='" + specialization + '\'' +
                ", user=" + user +
                '}';
    }
}
