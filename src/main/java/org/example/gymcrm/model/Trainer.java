package org.example.gymcrm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.gymcrm.model.group.OnUpdate;

import java.util.Objects;

@Entity
@Table(name = "trainers")
public class Trainer {
    @Id
    private Long id;
    @NotBlank(message = "Specialization is a mandatory field", groups = OnUpdate.class)
    private String specialization;

    @NotNull(message = "User must not be null")
    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    public Trainer(Long id, String specialization, User user) {
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
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
