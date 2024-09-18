package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    Booking addBooking(long userId, RequestBookingDto requestBookingDto);

    Booking handleBookingApproval(long userId, long bookingId, boolean approved);

    Booking getUserBookingById(long userId, long bookingId);

    List<Booking> getAllUserBooking(long userId, String state);

    List<Booking> getAllOwnerBooking(long ownerId, String state);

    BookingDtoForItem getBookingForItem(long itemId, String booking);

    List<Booking> getBookingsForComment(long itemId, long userId);
}