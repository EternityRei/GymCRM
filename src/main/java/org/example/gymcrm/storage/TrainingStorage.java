package org.example.gymcrm.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.gymcrm.model.Training;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TrainingStorage {
    private final Map<String, Training> trainingMap = new ConcurrentHashMap<>();

    @Value("${training.initial.data.path}")
    private Resource initialTrainingFile;

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Training> trainees = mapper.readValue(initialTrainingFile.getFile(), new TypeReference<List<Training>>() {});
        trainees.forEach(this::save);
    }

    public Training save(Training training) {
        trainingMap.put(training.getTrainingId(), training);
        return training;
    }

    public Training findById(String id) {
        return trainingMap.get(id);
    }

    public List<Training> findAll() {
        return new ArrayList<>(trainingMap.values());
    }

    public void deleteById(String id) {
        trainingMap.remove(id);
    }
}
