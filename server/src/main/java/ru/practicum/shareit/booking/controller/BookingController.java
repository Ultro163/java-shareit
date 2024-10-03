package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.mapper.BookingMapper;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingServiceImpl;
    private final BookingMapper bookingMapper;

    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @RequestBody RequestBookingDto requestBookingDto) {
        return bookingMapper.mapToBookingDto(bookingServiceImpl.addBooking(userId, requestBookingDto));
    }

    @PatchMapping("/{bookingId}")
    public BookingDto handleBookingApproval(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @PathVariable long bookingId,
                                            @RequestParam boolean approved) {
        return bookingMapper.mapToBookingDto(bookingServiceImpl.handleBookingApproval(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingDto getUserBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @PathVariable long bookingId) {
        return bookingMapper.mapToBookingDto(bookingServiceImpl.getUserBookingById(userId, bookingId));
    }

    @GetMapping
    public List<BookingDto> getAllUserBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(defaultValue = "ALL") String state,
                                              @RequestParam(required = false) Integer from,
                                              @RequestParam(required = false) Integer size) {
        State stateEnum = State.fromString(state.toUpperCase());
        return bookingServiceImpl.getAllUserBooking(userId, stateEnum, from, size)
                .stream().map(bookingMapper::mapToBookingDto).toList();
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllOwnerBooking(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                               @RequestParam(defaultValue = "ALL") String state,
                                               @RequestParam(required = false) Integer from,
                                               @RequestParam(required = false) Integer size) {
        State stateEnum = State.fromString(state.toUpperCase());
        return bookingServiceImpl.getAllOwnerBooking(ownerId, stateEnum, from, size)
                .stream().map(bookingMapper::mapToBookingDto).toList();
    }
}