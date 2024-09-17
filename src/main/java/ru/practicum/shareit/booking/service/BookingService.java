package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(long userId, RequestBookingDto requestBookingDto);

    BookingDto handleBookingApproval(long userId, long bookingId, boolean approved);

    BookingDto getUserBookingById(long userId, long bookingId);

    List<BookingDto> getAllUserBooking(long userId, String state);

    BookingDto getAllOwnerBooking(long ownerId, String state);
}