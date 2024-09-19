package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {

    Booking addBooking(long bookerId, RequestBookingDto requestBookingDto);

    Booking handleBookingApproval(long ownerId, long bookingId, boolean approved);

    Booking getUserBookingById(long bookerId, long bookingId);

    List<Booking> getAllUserBooking(long userId, State state);

    List<Booking> getAllOwnerBooking(long ownerId, State state);

    BookingDtoForItem getBookingForItem(long itemId, String booking);

    List<Booking> getBookingsForComment(long itemId, long bookerId);
}