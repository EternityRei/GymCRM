package org.example.gymcrm.service.impl;

import org.example.gymcrm.dao.UserDao;
import org.example.gymcrm.model.User;
import org.example.gymcrm.service.UserCredentialsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserCredentialsServiceImpl implements UserCredentialsService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);

    private final UserDao userDao;

    @Autowired
    public UserCredentialsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public User createCredentials(User user) {
        logger.info("Creating user: {}.{}", user.getFirstName(), user.getLastName());

        // Initial username calculation without the suffix
        String baseUsername = user.getFirstName() + "." + user.getLastName();
        List<User> existingUsers = userDao.findUsers(user);

        // Extracting existing suffixes and finding the next available suffix
        List<Integer> sortedSuffixes = extractSuffixes(existingUsers, baseUsername);

        //Calculate suffix
        int suffix = calculateSuffix(sortedSuffixes);

        // Apply the suffix to the username if necessary and get a final username
        String finalUsername = createUsernameAndPassword(user, baseUsername, suffix);

        userDao.save(user);
        logger.info("{} created with username: {}", user.getClass().getName(), finalUsername);
        return user;
    }

    private String generateRandomPassword() {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    private List<Integer> extractSuffixes(List<User> existingUsers, String baseUsername) {
        return existingUsers.stream()
               .map(u -> {
                   String username = u.getUsername();
                   if (username.startsWith(baseUsername)) {
                       String suffix = username.substring(baseUsername.length());
                       if (suffix.isEmpty()) {
                           return 0; // No suffix
                       } else {
                           try {
                               return Integer.parseInt(suffix); // Parse the suffix
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
    }

    private String createUsernameAndPassword(User user, String baseUsername, int suffix){
        String finalUsername = baseUsername + (suffix > 0 ? suffix : "");
        user.setUsername(finalUsername);
        String password = generateRandomPassword();
        user.setPassword(password);
        return finalUsername;
    }

    private int calculateSuffix(List<Integer> sortedSuffixes) {
        int suffix = 0; // Start with 0 for the suffix, implying no suffix initially
        if (!sortedSuffixes.isEmpty()) {
            suffix = sortedSuffixes.get(sortedSuffixes.size() - 1) + 1; // Increment the highest suffix
        }
        return suffix;
    }
}
