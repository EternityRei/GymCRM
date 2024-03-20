package org.example.gymcrm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.gymcrm.model.group.OnCreate;
import org.example.gymcrm.model.group.OnUpdate;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "trainees")
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    @NotBlank(message = "Date of birth is a mandatory field", groups = {OnCreate.class, OnUpdate.class})
    private String dateOfBirth;
    @NotBlank(message = "Address is a mandatory field", groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 150, message = "Maximum address field length is 150 characters",  groups = {OnCreate.class, OnUpdate.class})
    private String address;

    @NotNull(message = "User must not be null")
    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "trainee", fetch = FetchType.EAGER, orphanRemoval = true)
    List<Training> trainingList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    private Set<Trainer> trainers;

    public Trainee(Long id, String dateOfBirth, String address, User user) {
        this.id = id;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.user = user;
    }

    public Trainee(){
    }

    public Long getId() {
        return id;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Trainer> getTrainers() {
        return trainers;
    }

    public void setTrainers(Set<Trainer> trainers) {
        this.trainers = trainers;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trainee trainee)) return false;
        return Objects.equals(id, trainee.id) && Objects.equals(getDateOfBirth(), trainee.getDateOfBirth()) && Objects.equals(getAddress(), trainee.getAddress()) && Objects.equals(getUser(), trainee.getUser()) && Objects.equals(trainingList, trainee.trainingList) && Objects.equals(getTrainers(), trainee.getTrainers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getDateOfBirth(), getAddress(), getUser(), trainingList, getTrainers());
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "id=" + id +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", address='" + address + '\'' +
                ", user=" + user +
                ", trainingList=" + trainingList +
                ", trainers=" + trainers +
                '}';
    }
}
