package org.example.gymcrm.service;

import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.model.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;

@Service
public class TraineeServiceImpl implements TraineeService {

    private final TraineeDao traineeDao;
    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);

    @Autowired
    public TraineeServiceImpl(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Override
    public Trainee createTrainee(Trainee trainee) {
        logger.info("Creating trainee: {}.{}", trainee.getFirstName(), trainee.getLastName());

        // Initial username calculation without the suffix
        String baseUsername = trainee.getFirstName() + "." + trainee.getLastName();
        List<Trainee> existingTrainees = traineeDao.findByFirstNameAndLastNameStartingWith(trainee.getFirstName(), trainee.getLastName());

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

        int suffix = 0;
        if (!sortedSuffixes.isEmpty()) {
            suffix = sortedSuffixes.get(sortedSuffixes.size() - 1) + 1; // Increment the highest suffix
        }

        String finalUsername = baseUsername + (suffix > 0 ? suffix : "");
        trainee.setUsername(finalUsername);
        String password = generateRandomPassword();
        trainee.setPassword(password);

        traineeDao.save(trainee);
        logger.info("Trainee created with username: {}", finalUsername);
        return trainee;
    }

    //Noa.Lee -> Noa.Lee1 -> Noa.Lee2 -> delete Noa.Lee -> add Noa.Lee => Noa.Lee3



    @Override
    public Trainee updateTrainee(String id, Trainee updatedTrainee) {
        logger.info("Updating trainee with ID: {}", id);
        Trainee existingTrainee = traineeDao.findById(id);
        if (existingTrainee != null) {

            existingTrainee.setFirstName(updatedTrainee.getFirstName());
            existingTrainee.setLastName(updatedTrainee.getLastName());
            existingTrainee.setUsername(updatedTrainee.getUsername());
            existingTrainee.setPassword(updatedTrainee.getPassword());
            existingTrainee.setActive(updatedTrainee.isActive());
            existingTrainee.setDateOfBirth(updatedTrainee.getDateOfBirth());
            existingTrainee.setAddress(updatedTrainee.getAddress());

            traineeDao.save(existingTrainee);
            logger.info("Trainee with ID: {} updated successfully", id);
            return existingTrainee;
        } else {
            logger.error("Failed to update trainee. Trainee with ID: {} not found.", id);
            throw new RuntimeException("Trainee with ID " + id + " not found.");
        }
    }


    @Override
    public void deleteTrainee(String id) {
        logger.info("Deleting trainee with ID: {}", id);
        Trainee existingTrainee = traineeDao.findById(id);
        if (existingTrainee != null) {
            traineeDao.deleteById(id);
        } else {
            logger.error("Failed to delete trainee. Trainee with ID: {} not found.", id);
            throw new RuntimeException("Entity was not found");
        }
    }

    @Override
    public Trainee getTrainee(String id) {
        logger.info("Fetching trainee with ID: {}", id);
        return traineeDao.findById(id);
    }

    @Override
    public List<Trainee> getAllTrainees() {
        logger.info("Fetching all trainers");
        return traineeDao.findAll();
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

