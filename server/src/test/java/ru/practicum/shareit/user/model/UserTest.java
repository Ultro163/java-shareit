package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserTest {

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user2 = new User();
    }

    @Test
    void testEquals_SameObject() {
        assertEquals(user1, user1);
    }

    @Test
    void testEquals_EqualObjects() {
        user1.setId(1L);
        user2.setId(1L);

        assertEquals(user1, user2);
    }

    @Test
    void testEquals_DifferentObjects() {
        user1.setId(1L);
        user2.setId(2L);

        assertNotEquals(user1, user2);
    }

    @Test
    void testEquals_OneObjectHasNullId() {
        user1.setId(null);
        user2.setId(1L);

        assertNotEquals(user1, user2);
    }

    @Test
    void testEquals_BothIdsAreNull() {
        user1.setId(null);
        user2.setId(null);

        assertNotEquals(user1, user2);
    }

    @Test
    void testEquals_NullObject() {
        assertNotEquals(user1, null);
    }

    @Test
    void testEquals_DifferentClass() {
        assertNotEquals(user1, new Object());
    }

    @Test
    void testHashCode_SameObject() {
        user1.setId(1L);
        assertEquals(user1.hashCode(), user1.hashCode());
    }

    @Test
    void testHashCode_EqualObjects() {
        user1.setId(1L);
        user2.setId(1L);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}