package org.example.gymcrm.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "training_types")
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "training_type_name", nullable = false)
    private String name;

    public TrainingType(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TrainingType(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
