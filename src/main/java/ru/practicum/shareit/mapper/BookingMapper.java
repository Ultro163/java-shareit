package ru.practicum.shareit.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.RequestBookingDto;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking requestBookingDtoMapToBooking(RequestBookingDto requestBookingDto);

    BookingDto mapToBookingDto(Booking booking);
}