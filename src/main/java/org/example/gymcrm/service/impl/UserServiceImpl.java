package org.example.gymcrm.service.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.example.gymcrm.model.User;
import org.example.gymcrm.model.group.OnUpdate;
import org.example.gymcrm.repository.UserRepository;
import org.example.gymcrm.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);

    private final UserRepository userRepository;
    private final Validator validator;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, Validator validator) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @Override
    public String createUsername(String firstname, String lastname) {
        logger.debug("Creating username for user with first name: {} and last name: {}", firstname, lastname);

        String baseUsername = firstname + "." + lastname;
        String finalUsername = getFinalUsername(baseUsername);

        logger.info("Generated username: {}", finalUsername);
        return finalUsername;
    }


    @Override
    public void updatePassword(String id, String newPassword) {
        logger.info("Changing password for user with id={}", id);
        User user = userRepository.findById(Long.valueOf(id)).orElseThrow(() -> new RuntimeException("User not found"));

        // Temporarily update password for validation
        String originalPassword = user.getPassword();
        user.setPassword(newPassword);

        validatePassword(user, originalPassword);

        userRepository.save(user);
    }

    private void validatePassword(User user, String originalPassword) {
        Set<ConstraintViolation<User>> violations = validator.validate(user, OnUpdate.class);
        if (!violations.isEmpty()) {
            // Reset to original password if validation fails
            user.setPassword(originalPassword);

            String violationMessages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce((message1, message2) -> message1 + ", " + message2)
                    .orElse("Validation error");
            throw new RuntimeException("Validation error: " + violationMessages);
        }
    }

    @Override
    public boolean banUser(String id) {
        logger.info("Changing account status for user with id={}", id);
        User user = userRepository.findById(Long.valueOf(id)).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(!user.isActive());
        return user.isActive();
    }

    private String getFinalUsername(String baseUsername) {
        logger.debug("Determining final username for base: {}", baseUsername);

        List<Integer> sortedSuffixes = extractSuffixes(baseUsername);
        int suffix = calculateSuffix(sortedSuffixes);
        String finalUsername = baseUsername + (suffix > 0 ? Integer.toString(suffix) : "");

        logger.debug("Final username determined as: {}", finalUsername);
        return finalUsername;
    }


    public String generateRandomPassword() {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    private List<Integer> extractSuffixes(String baseUsername) {
        logger.debug("Extracting suffixes for base username: {}", baseUsername);

        List<User> existingUsers = userRepository.findAll();
        List<Integer> suffixes = existingUsers.stream()
                .map(u -> findExistedSuffixes(baseUsername, u))
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toList());

        logger.debug("Extracted {} suffixes for base username: {}", suffixes.size(), baseUsername);
        return suffixes;
    }


    private Integer findExistedSuffixes(String baseUsername, User u) {
        String username = u.getUsername();
        if (username.startsWith(baseUsername)) {
            String suffix = username.substring(baseUsername.length());
            if (suffix.isEmpty()) {
                return 0; // No suffix implies the base username itself is in use
            } else {
                try {
                    return Integer.parseInt(suffix);
                } catch (NumberFormatException e) {
                    // This could log potentially large numbers of errors if non-numeric suffixes are common
                    logger.debug("Non-numeric suffix encountered for username: {} with suffix: {}", username, suffix);
                    return null;
                }
            }
        }
        return null;
    }


    private int calculateSuffix(List<Integer> sortedSuffixes) {
        int suffix = sortedSuffixes.isEmpty() ? 0 : sortedSuffixes.get(sortedSuffixes.size() - 1) + 1;

        logger.debug("Calculated suffix: {}", suffix);
        return suffix;
    }

}
