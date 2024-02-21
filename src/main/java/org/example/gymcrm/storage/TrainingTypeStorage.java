package org.example.gymcrm.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.gymcrm.model.Training;
import org.example.gymcrm.model.TrainingType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TrainingTypeStorage {
    private final Map<String, TrainingType> trainingTypeMap = new ConcurrentHashMap<>();

    @Value("${trainingType.initial.data.path}")
    private Resource initialTraineesFile;

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<TrainingType> trainers = mapper.readValue(initialTraineesFile.getFile(), new TypeReference<List<TrainingType>>() {});
        trainers.forEach(this::save);
    }

    public TrainingType save(TrainingType trainingType) {
        trainingTypeMap.put(trainingType.getId(), trainingType);
        return trainingType;
    }

    public TrainingType findById(String id) {
        return trainingTypeMap.get(id);
    }

    public List<TrainingType> findAll() {
        return new ArrayList<>(trainingTypeMap.values());
    }
}
