package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userServiceImpl;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto addBooking(long userId, RequestBookingDto requestBookingDto) {
        Booking booking = bookingMapper.requestBookingDtoMapToBooking(requestBookingDto);
        User booker = userServiceImpl.checkUserExist(userId);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        Booking saveBooking = bookingRepository.save(booking);
        return bookingMapper.mapToBookingDto(saveBooking);
    }
}
