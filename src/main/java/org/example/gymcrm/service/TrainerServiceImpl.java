package org.example.gymcrm.service;

import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.model.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;

@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerDao trainerDao;
    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);


    @Autowired
    public TrainerServiceImpl(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Override
    public Trainer createTrainer(Trainer trainer) {
        logger.info("Creating trainer: {}.{}", trainer.getFirstName(), trainer.getLastName());

        // Initial username calculation without the suffix
        String baseUsername = trainer.getFirstName() + "." + trainer.getLastName();
        List<Trainer> existingTrainees = trainerDao.findByFirstNameAndLastNameStartingWith(trainer.getFirstName(), trainer.getLastName());

        // Extracting existing suffixes and finding the next available suffix
        List<Integer> sortedSuffixes = existingTrainees.stream()
                .map(t -> {
                    String username = t.getUsername();
                    if (username.startsWith(baseUsername)) {
                        String suffix = username.substring(baseUsername.length());
                        if (suffix.isEmpty()) {
                            return 0; // No suffix
                        } else {
                            try {
                                return Integer.parseInt(suffix); // Skip the dot and parse the suffix
                            } catch (NumberFormatException e) {
                                return null;
                            }
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .sorted()
                .toList();

        int suffix = 0; // Start with 0 for the suffix, implying no suffix initially
        if (!sortedSuffixes.isEmpty()) {
            suffix = sortedSuffixes.get(sortedSuffixes.size() - 1) + 1; // Increment the highest suffix
        }

        // Apply the suffix to the username if necessary
        String finalUsername = baseUsername + (suffix > 0 ? "." + suffix : "");
        trainer.setUsername(finalUsername);
        String password = generateRandomPassword();
        trainer.setPassword(password);

        trainerDao.save(trainer);
        logger.info("Trainer created with username: {}", finalUsername);
        return trainer;
    }

    @Override
    public Trainer updateTrainer(String id, Trainer trainer) {
        logger.info("Updating trainer with ID: {}", id);
        Trainer existingTrainer = trainerDao.findById(id);
        if (existingTrainer != null) {

            existingTrainer.setFirstName(trainer.getFirstName());
            existingTrainer.setLastName(trainer.getLastName());
            existingTrainer.setUsername(trainer.getUsername());
            existingTrainer.setPassword(trainer.getPassword());
            existingTrainer.setActive(trainer.isActive());
            existingTrainer.setSpecialization(trainer.getSpecialization());

            trainerDao.save(existingTrainer);
            logger.info("Trainer with ID: {} updated successfully", id);
            return existingTrainer;
        } else {
            logger.error("Failed to update trainer. Trainer with ID: {} not found.", id);
            throw new RuntimeException("Trainer with ID " + id + " not found.");
        }
    }

    @Override
    public Trainer getTrainer(String id) {
        logger.info("Fetching trainer with ID: {}", id);
        return trainerDao.findById(id);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        logger.info("Fetching all trainers");
        return trainerDao.findAll();
    }

    private String generateRandomPassword() {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }
}

