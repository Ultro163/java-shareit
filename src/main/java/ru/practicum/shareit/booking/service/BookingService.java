package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;

public interface BookingService {

    BookingDto addBooking(long userId, RequestBookingDto requestBookingDto);
}
