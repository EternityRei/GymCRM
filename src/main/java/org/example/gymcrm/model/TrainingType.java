package org.example.gymcrm.model;

public class TrainingType {
    private String id;
    private String name;

    public TrainingType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public TrainingType(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
