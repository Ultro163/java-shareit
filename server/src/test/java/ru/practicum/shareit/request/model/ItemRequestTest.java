package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ItemRequestTest {

    private ItemRequest itemRequest1;
    private ItemRequest itemRequest2;

    @BeforeEach
    void setUp() {
        itemRequest1 = new ItemRequest();
        itemRequest2 = new ItemRequest();
    }

    @Test
    void testEquals_SameObject() {
        assertEquals(itemRequest1, itemRequest1);
    }

    @Test
    void testEquals_EqualObjects() {
        itemRequest1.setId(1L);
        itemRequest2.setId(1L);

        assertEquals(itemRequest1, itemRequest2);
    }

    @Test
    void testEquals_DifferentObjects() {
        itemRequest1.setId(1L);
        itemRequest2.setId(2L);

        assertNotEquals(itemRequest1, itemRequest2);
    }

    @Test
    void testEquals_OneObjectHasNullId() {
        itemRequest1.setId(null);
        itemRequest2.setId(1L);

        assertNotEquals(itemRequest1, itemRequest2);
    }

    @Test
    void testEquals_BothIdsAreNull() {
        itemRequest1.setId(null);
        itemRequest2.setId(null);

        assertNotEquals(itemRequest1, itemRequest2);
    }

    @Test
    void testEquals_NullObject() {
        assertNotEquals(itemRequest1, null);
    }

    @Test
    void testEquals_DifferentClass() {
        assertNotEquals(itemRequest1, new Object());
    }

    @Test
    void testHashCode_SameObject() {
        itemRequest1.setId(1L);

        assertEquals(itemRequest1.hashCode(), itemRequest1.hashCode());
    }

    @Test
    void testHashCode_EqualObjects() {
        itemRequest1.setId(1L);
        itemRequest2.setId(1L);

        assertEquals(itemRequest1.hashCode(), itemRequest2.hashCode());
    }
}