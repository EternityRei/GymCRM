package org.example.gymcrm.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.Training;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class TrainerStorage {
    private final Map<String, Trainer> trainerMap = new ConcurrentHashMap<>();

    @Value("${trainers.initial.data.path}")
    private Resource initialTrainersFile;

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Trainer> trainers = mapper.readValue(initialTrainersFile.getFile(), new TypeReference<List<Trainer>>() {});
        trainers.forEach(this::save);
    }

    public Trainer save(Trainer trainer) {
        trainerMap.put(trainer.getId(), trainer);
        return trainer;
    }

    public Trainer findById(String id) {
        return trainerMap.get(id);
    }

    public List<Trainer> findAll() {
        return new ArrayList<>(trainerMap.values());
    }

    public void deleteById(String id) {
        trainerMap.remove(id);
    }

    public List<Trainer> findByFirstNameAndLastNameStartingWith(String firstName, String lastNamePrefix) {
        return trainerMap.values().stream()
                .filter(trainee -> trainee.getFirstName().equals(firstName) &&
                        trainee.getLastName().startsWith(lastNamePrefix))
                .collect(Collectors.toList());
    }
}
