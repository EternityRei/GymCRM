package org.example.gymcrm.service.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.example.gymcrm.model.User;
import org.example.gymcrm.model.group.OnUpdate;
import org.example.gymcrm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void whenCreateUsername_thenSuccess() {
        String firstname = "John";
        String lastname = "Doe";
        // Assume no users with similar names exist for simplification

        String expectedUsername = firstname + "." + lastname;

        String result = userService.createUsername(firstname, lastname);

        assertEquals(expectedUsername, result);
    }

    @Test
    void whenUpdatePasswordWithInvalid_thenValidationFails() {
        String userId = "1";
        User user = new User();
        user.setId(Long.valueOf(userId));
        user.setPassword("originalPassword");

        when(userRepository.findById(Long.valueOf(userId))).thenReturn(Optional.of(user));

        // Simulate a validation error
        ConstraintViolation<User> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Password validation message");
        when(validator.validate(any(User.class), eq(OnUpdate.class))).thenReturn(Set.of(violation));

        Exception exception = assertThrows(RuntimeException.class, () -> userService.updatePassword(userId, "invalid"));
        assertTrue(exception.getMessage().contains("Validation error"));
        assertEquals("originalPassword", user.getPassword()); // Ensure password is not changed
    }

    @Test
    void whenBanUser_thenSuccess() {
        String userId = "1";
        User user = new User();
        user.setId(Long.valueOf(userId));
        user.setActive(true);

        when(userRepository.findById(Long.valueOf(userId))).thenReturn(Optional.of(user));

        userService.modifyAccountStatus(userId);

        assertFalse(user.isActive());
    }

    @Test
    void whenGenerateRandomPassword_thenCorrectLength() {
        String password = userService.generateRandomPassword();

        assertNotNull(password);
        assertEquals(10, password.length()); // Assuming the method generates a 10-character password
    }
    @Test
    void whenCreateUsernameWithExistingUser_thenSuffixAdded() {
        String firstname = "Jane";
        String lastname = "Doe";
        List<User> existingUsers = List.of(
                createUser("Jane.Doe"),
                createUser("Jane.Doe1")
        );
        when(userRepository.findAll()).thenReturn(existingUsers);

        String result = userService.createUsername(firstname, lastname);

        assertEquals("Jane.Doe2", result);
    }

    private User createUser(String username) {
        User user = new User();
        user.setUsername(username);
        return user;
    }

    @Test
    void whenUpdatePasswordForNonExistingUser_thenThrowException() {
        String userId = "2";
        when(userRepository.findById(Long.valueOf(userId))).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> userService.updatePassword(userId, "newPassword123"));

        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    void whenBanNonExistingUser_thenThrowException() {
        String userId = "3";
        when(userRepository.findById(Long.valueOf(userId))).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> userService.modifyAccountStatus(userId));

        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    void whenGenerateMultipleRandomPasswords_thenTheyAreUnique() {
        String password1 = userService.generateRandomPassword();
        String password2 = userService.generateRandomPassword();
        String password3 = userService.generateRandomPassword();

        assertNotEquals(password1, password2);
        assertNotEquals(password1, password3);
        assertNotEquals(password2, password3);
    }

    @Test
    void whenUpdatePassword_thenOtherFieldsNotAffected() {
        String userId = "1";
        User user = new User();
        user.setId(Long.valueOf(userId));
        user.setUsername("johndoe");
        user.setActive(true);
        user.setPassword("original");

        when(userRepository.findById(Long.valueOf(userId))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updatePassword(userId, "newPass");

        assertEquals("johndoe", user.getUsername());
        assertTrue(user.isActive());
        assertEquals("newPass", user.getPassword());
        verify(userRepository, times(1)).save(user); // Ensures save was called
    }
}
