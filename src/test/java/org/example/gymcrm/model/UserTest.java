package org.example.gymcrm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("1", "John", "Doe", "john.doe", "password123", true);
    }

    @Test
    void testUserConstructorAndProperties() {
        assertEquals("1", user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertTrue(user.isActive());
    }

    @Test
    void testSettersAndGetters() {
        user.setId("2");
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setUsername("jane.smith");
        user.setPassword("secret");
        user.setActive(false);

        assertEquals("2", user.getId());
        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("jane.smith", user.getUsername());
        assertEquals("secret", user.getPassword());
        assertFalse(user.isActive());
    }

    @Test
    void testEquals() {
        User anotherUser = new User("1", "John", "Doe", "john.doe", "password123", true);
        assertEquals(user, anotherUser);
    }

    @Test
    void testHashCode() {
        User anotherUser = new User("1", "John", "Doe", "john.doe", "password123", true);
        assertEquals(user.hashCode(), anotherUser.hashCode());
    }

    @Test
    void testToString() {
        String expected = "User{id='1', firstName='John', lastName='Doe', username='john.doe', password='password123', isActive=true}";
        assertEquals(expected, user.toString());
    }
}
