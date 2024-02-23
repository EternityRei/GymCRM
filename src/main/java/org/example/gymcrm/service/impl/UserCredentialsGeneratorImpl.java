package org.example.gymcrm.service.impl;

import org.example.gymcrm.dao.UserDao;
import org.example.gymcrm.model.User;
import org.example.gymcrm.service.UserCredentialsGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;

@Service
public class UserCredentialsGeneratorImpl implements UserCredentialsGenerator {

    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);

    private UserDao userDao;

    @Autowired
    public UserCredentialsGeneratorImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public String createUsername(String firstname, String lastname) {
        logger.info("Creating user: {}.{}", firstname, lastname);

        // Initial username calculation without the suffix
        String baseUsername = firstname + "." + lastname;

        return getFinalUsername(baseUsername);
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
        List<User> existingUsers = userDao.findUsers();
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

    private int calculateSuffix(List<Integer> sortedSuffixes) {
        int suffix = 0; // Start with 0 for the suffix, implying no suffix initially
        if (!sortedSuffixes.isEmpty()) {
            suffix = sortedSuffixes.get(sortedSuffixes.size() - 1) + 1; // Increment the highest suffix
        }
        return suffix;
    }
}
