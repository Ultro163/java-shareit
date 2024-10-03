package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.service.BookingClient;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @Valid @RequestBody RequestBookingDto requestBookingDto) {
        return bookingClient.addBooking(userId, requestBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> handleBookingApproval(@RequestHeader("X-Sharer-User-Id") long userId,
                                                        @PathVariable long bookingId,
                                                        @RequestParam boolean approved) {
        return bookingClient.handleBookingApproval(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getUserBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @PathVariable long bookingId) {
        return bookingClient.getUserBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @RequestParam(name = "state", defaultValue = "all") String state,
                                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                    @Positive @RequestParam(name = "size", defaultValue = "100") Integer size) {
        State stateEnum = State.fromString(state.toUpperCase());
        return bookingClient.getAllUserBooking(userId, stateEnum, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllOwnerBooking(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                     @RequestParam(name = "state", defaultValue = "all") String state,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "100") Integer size) {
        State stateEnum = State.fromString(state.toUpperCase());
        return bookingClient.getAllOwnerBooking(ownerId, stateEnum, from, size);
    }
}