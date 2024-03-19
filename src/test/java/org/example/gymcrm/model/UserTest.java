package org.example.gymcrm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "John", "Doe", "johndoe", "password123", true);
    }

    @Test
    void getId_ReturnsCorrectId() {
        assertEquals(1L, user.getId());
    }

    @Test
    void getAndSetFirstName() {
        user.setFirstName("Jane");
        assertEquals("Jane", user.getFirstName());
    }

    @Test
    void getAndSetLastName() {
        user.setLastName("Smith");
        assertEquals("Smith", user.getLastName());
    }

    @Test
    void getAndSetUsername() {
        user.setUsername("janesmith");
        assertEquals("janesmith", user.getUsername());
    }

    @Test
    void getAndSetPassword() {
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    void isActive_ReturnsTrue() {
        assertTrue(user.isActive());
    }

    @Test
    void setActive_SetsCorrectly() {
        user.setActive(false);
        assertFalse(user.isActive());
    }

    @Test
    void testEquals_Symmetric() {
        User anotherUser = new User(1L, "John", "Doe", "johndoe", "password123", true);
        assertTrue(user.equals(anotherUser) && anotherUser.equals(user));
        assertEquals(user.hashCode(), anotherUser.hashCode());
    }

    @Test
    void testToString_ContainsRelevantInfo() {
        String toStringResult = user.toString();
        assertTrue(toStringResult.contains("John") && toStringResult.contains("Doe") && toStringResult.contains("johndoe"));
    }
}
