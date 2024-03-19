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
        logger.info("Creating user: {}.{}", firstname, lastname);

        // Initial username calculation without the suffix
        String baseUsername = firstname + "." + lastname;

        return getFinalUsername(baseUsername);
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
    public void banUser(String id) {
        logger.info("Changing account status for user with id={}", id);
        User user = userRepository.findById(Long.valueOf(id)).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(!user.isActive());
    }

    private String getFinalUsername(String baseUsername) {
        // Extracting existing suffixes and finding the next available suffix
        List<Integer> sortedSuffixes = extractSuffixes(baseUsername);

        //Calculate suffix
        int suffix = calculateSuffix(sortedSuffixes);

        // Apply the suffix to the username if necessary and get a final username
        return baseUsername + (suffix > 0 ? suffix : "");
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
        List<User> existingUsers = userRepository.findAll();
        return existingUsers.stream()
               .map(u -> findExistedSuffixes(baseUsername, u))
               .filter(Objects::nonNull)
               .sorted()
               .toList();
    }

    private Integer findExistedSuffixes(String baseUsername, User u) {
        String username = u.getUsername();
        if (username.startsWith(baseUsername)) {
            String suffix = username.substring(baseUsername.length());
            if (suffix.isEmpty()) {
                return 0;
            } else {
                try {
                    return Integer.parseInt(suffix);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }

    private int calculateSuffix(List<Integer> sortedSuffixes) {
        int suffix = 0; // Start with 0 for the suffix, implying no suffix initially
        if (!sortedSuffixes.isEmpty()) {
            suffix = sortedSuffixes.get(sortedSuffixes.size() - 1) + 1; // Increment the highest suffix
        }
        return suffix;
    }
}
