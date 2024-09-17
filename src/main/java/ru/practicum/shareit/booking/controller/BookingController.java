package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
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
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingServiceImpl;

    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @Valid @RequestBody RequestBookingDto requestBookingDto) {
        return bookingServiceImpl.addBooking(userId, requestBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto handleBookingApproval(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @PathVariable long bookingId,
                                            @RequestParam boolean approved) {
        return bookingServiceImpl.handleBookingApproval(userId, bookingId, approved);
    }

    @GetMapping("{bookingId}")
    public BookingDto getUserBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @PathVariable long bookingId) {
        return bookingServiceImpl.getUserBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getAllUserBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(defaultValue = "ALL") String state) {
        return bookingServiceImpl.getAllUserBooking(userId, state);
    }

    @GetMapping("/owner")
    public BookingDto getAllOwnerBooking(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                         @RequestParam(defaultValue = "ALL") String state) {
        return bookingServiceImpl.getAllOwnerBooking(ownerId, state);
    }

}