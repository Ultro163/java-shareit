package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.util.Date;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private Long id;
    private Date start;
    private Date end;
    private Item item;
    private Long booker;
    private BookingStatus status;
}
