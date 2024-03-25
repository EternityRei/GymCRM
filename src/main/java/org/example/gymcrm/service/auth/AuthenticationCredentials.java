package org.example.gymcrm.service.auth;

import org.example.gymcrm.model.Trainee;
import org.example.gymcrm.model.Trainer;
import org.example.gymcrm.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticationCredentials {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationCredentials.class);

    public Map<String, String> getCredentials(Object[] args) {
        log.debug("Starting getCredentials with args length: {}", args.length);

        checkArgsLength(args);
        log.debug("Arguments length check passed");

        Map<String, String> map = new HashMap<>();

        log.debug("Extracting credentials");
        String[] credentials = extractCredentials(args);

        assert credentials != null;
        log.debug("Credentials extracted successfully");

        map.put("username", credentials[0]);
        map.put("password", credentials[1]);

        return map;
    }

    private void checkArgsLength(Object[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Authentication credentials not provided");
        }
    }


    private String[] extractCredentials(Object[] args) {
        log.debug("Starting to extract credentials from arguments");
        String[] credentials = null;

        for (Object arg : args) {
            if (arg instanceof  String) {
                log.debug("Extracting credentials based on a String argument");
                credentials = getCredentialsForString(args, (String) arg);
                break;
            } else {
                log.debug("Extracting credentials for a User object");
                User user = getUser(arg);
                credentials = getCredentialsForObject(user);
                break;
            }
        }

        if (credentials != null) {
            log.debug("Credentials extracted successfully");
        } else {
            log.debug("No credentials found in the provided arguments");
        }

        return credentials;
    }

    private String[] getCredentialsForObject(User user) {
        return new String[]{user.getUsername(), user.getPassword()};
    }

    private String[] getCredentialsForString(Object[] args, String arg) {
        log.debug("Starting to extract credentials using a string argument.");
        String username = null;
        String password = null;

        int index = Arrays.asList(args).indexOf(arg);
        log.debug("Index of the provided argument in args array: {}", index);

        if (index >= 0 && index < args.length - 1) {
            log.debug("Argument is within bounds. Checking for subsequent string argument...");
            if (args[index + 1] instanceof String) {
                username = arg;
                password = (String) args[index + 1];
                log.debug("Username and password successfully identified.");
            } else {
                log.debug("Next argument after username is not a string. Cannot extract password.");
            }
        } else {
            log.debug("Argument index is out of bounds for extracting a subsequent password.");
        }

        assert username != null : "Username cannot be null after attempting credential extraction";
        assert password != null : "Password cannot be null after attempting credential extraction";

        return new String[]{username, password};
    }

    private User getUser(Object obj) {
        User user = new User();
        if (obj instanceof Trainee trainee) {
            user = trainee.getUser();
        }
        if (obj instanceof Trainer trainer) {
            user = trainer.getUser();
        }
        if (obj instanceof User user1) {
            user = user1;
        }
        return user;
    }

}
