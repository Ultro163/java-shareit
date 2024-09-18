package ru.practicum.shareit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking mapToBooking(RequestBookingDto requestBookingDto);

    BookingDto mapToBookingDto(Booking booking);

    @Mapping(target = "bookerId", source = "booker.id")
    BookingDtoForItem mapToBookingDtoForItem(Booking booking);
}