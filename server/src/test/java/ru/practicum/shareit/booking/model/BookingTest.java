package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookingTest {

    private Booking booking;
    private Item item;

    @BeforeEach
    void setUp() {
        item = mock(Item.class);
        User user = mock(User.class);

        booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);

        when(item.getId()).thenReturn(100L);
    }

    @Test
    void getItemId() {
        Long itemId = booking.getItemId();
        assertNotNull(itemId);
        assertEquals(100L, itemId);
        verify(item, times(1)).getId();
    }

    @Test
    void testEquals_SameObject() {
        assertEquals(booking, booking);
    }

    @Test
    void testEquals_DifferentObjectWithSameId() {
        Booking anotherBooking = new Booking();
        anotherBooking.setId(1L);

        assertEquals(booking, anotherBooking);
    }

    @Test
    void testEquals_DifferentObjectWithDifferentId() {
        Booking anotherBooking = new Booking();
        anotherBooking.setId(2L);

        assertNotEquals(booking, anotherBooking);
    }

    @Test
    void testEquals_Null() {
        assertNotEquals(booking, null);
    }

    @Test
    void testHashCode_SameObject() {
        Booking anotherBooking = new Booking();
        anotherBooking.setId(1L);

        assertEquals(booking.hashCode(), anotherBooking.hashCode());
    }
}