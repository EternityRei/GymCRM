package org.example.gymcrm.service.auth;

import org.example.gymcrm.model.User;
import org.example.gymcrm.repository.UserRepository;
import org.example.gymcrm.service.impl.TrainingTypeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authenticate(String username, String password) {
        logger.debug("Started authentication for user with username={}", username);
        User user = userRepository.findByUsername(username).orElseThrow();
        if (user.getPassword().equals(password)) {
            logger.info("User with username={} was authenticated successfully", username);
            return true;
        }
        logger.info("User with username={} was not authenticated", username);
        return false;
    }
}
