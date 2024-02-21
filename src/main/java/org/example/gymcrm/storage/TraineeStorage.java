package org.example.gymcrm.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.gymcrm.model.Trainee;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@PropertySource("classpath:application.properties")
public class TraineeStorage {
    private final Map<String, Trainee> traineeMap = new ConcurrentHashMap<>();

    @Value("${trainee.initial.data.path}")
    private Resource initialTraineesFile;

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Trainee> trainers = mapper.readValue(initialTraineesFile.getFile(), new TypeReference<List<Trainee>>() {});
        trainers.forEach(this::save);
    }

    public Trainee save(Trainee trainee) {
        traineeMap.put(trainee.getId(), trainee);
        return trainee;
    }

    public Trainee findById(String id) {
        return traineeMap.get(id);
    }

    public List<Trainee> findAll() {
        return new ArrayList<>(traineeMap.values());
    }

    public void deleteById(String id) {
        traineeMap.remove(id);
    }

    public List<Trainee> findByFirstNameAndLastNameStartingWith(String firstName, String lastNamePrefix) {
        return traineeMap.values().stream()
                .filter(trainee -> trainee.getFirstName().equals(firstName) &&
                        trainee.getLastName().startsWith(lastNamePrefix))
                .collect(Collectors.toList());
    }
}
