package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ItemTest {

    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        item1 = new Item();
        item2 = new Item();
    }

    @Test
    void testEquals_SameObject() {
        assertEquals(item1, item1);
    }

    @Test
    void testEquals_EqualObjects() {
        item1.setId(1L);
        item2.setId(1L);

        assertEquals(item1, item2);
    }

    @Test
    void testEquals_DifferentObjects() {
        item1.setId(1L);
        item2.setId(2L);

        assertNotEquals(item1, item2);
    }

    @Test
    void testEquals_OneObjectHasNullId() {
        item1.setId(null);
        item2.setId(1L);

        assertNotEquals(item1, item2);
    }

    @Test
    void testEquals_BothIdsAreNull() {
        item1.setId(null);
        item2.setId(null);

        assertNotEquals(item1, item2);
    }

    @Test
    void testEquals_NullObject() {
        assertNotEquals(item1, null);
    }

    @Test
    void testEquals_DifferentClass() {
        assertNotEquals(item1, new Object());
    }

    @Test
    void testHashCode_SameObject() {
        item1.setId(1L);

        assertEquals(item1.hashCode(), item1.hashCode());
    }

    @Test
    void testHashCode_EqualObjects() {
        item1.setId(1L);
        item2.setId(1L);

        assertEquals(item1.hashCode(), item2.hashCode());
    }
}