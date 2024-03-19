package org.example.gymcrm.service.auth;

import org.example.gymcrm.model.User;
import org.example.gymcrm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    @Autowired
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow();
        if (user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }
}
