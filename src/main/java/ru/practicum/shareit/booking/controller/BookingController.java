package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.dto.RequestBookingDto;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingServiceImpl;

    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @RequestBody RequestBookingDto requestBookingDto) {
        return bookingServiceImpl.addBooking(userId, requestBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto handleBookingApproval(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @PathVariable long bookingId,
                                            @RequestParam boolean approved) {
        return null;
    }

    @GetMapping("{bookingId}")
    public BookingDto getUserBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @PathVariable long bookingId) {
        return null;
    }

    @GetMapping
    public BookingDto getAllUserBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @RequestParam(defaultValue = "ALL") String state) {
        return null;
    }

    @GetMapping("/owner")
    public BookingDto getAllOwnerBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestParam(defaultValue = "ALL") String state) {
        return null;
    }

}