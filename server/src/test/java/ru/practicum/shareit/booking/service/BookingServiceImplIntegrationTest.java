package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.erorr.exception.AccessDeniedException;
import ru.practicum.shareit.erorr.exception.EntityNotFoundException;
import ru.practicum.shareit.erorr.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
class BookingServiceImplIntegrationTest {

    @Autowired
    private BookingServiceImpl bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User booker;
    private User owner;
    private Item item;
    private Booking booking;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        owner = userRepository.save(owner);

        booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@example.com");
        booker = userRepository.save(booker);

        item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Item Description");
        item.setAvailable(true);
        item.setOwner(owner);
        item = itemRepository.save(item);

        booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(BookingStatus.WAITING);
        booking = bookingRepository.save(booking);
    }

    @Test
    void testAddBookingSuccess() {
        RequestBookingDto requestBookingDto = RequestBookingDto.builder().build();
        requestBookingDto.setItemId(item.getId());
        requestBookingDto.setStart(LocalDateTime.now().plusDays(1));
        requestBookingDto.setEnd(LocalDateTime.now().plusDays(2));

        Booking newBooking = bookingService.addBooking(booker.getId(), requestBookingDto);

        assertNotNull(newBooking);
        assertEquals(BookingStatus.WAITING, newBooking.getStatus());
        assertEquals(booker.getId(), newBooking.getBooker().getId());
        assertEquals(item.getId(), newBooking.getItem().getId());
    }

    @Test
    void testHandleBookingApproval() {
        Booking approvedBooking = bookingService.handleBookingApproval(owner.getId(), booking.getId(), true);

        assertNotNull(approvedBooking);
        assertEquals(BookingStatus.APPROVED, approvedBooking.getStatus());
    }

    @Test
    void testGetUserBookingByIdSuccess() {
        Booking foundBooking = bookingService.getUserBookingById(booker.getId(), booking.getId());

        assertNotNull(foundBooking);
        assertEquals(booking.getId(), foundBooking.getId());
        assertEquals(BookingStatus.WAITING, foundBooking.getStatus());
    }

    @Test
    void testGetAllUserBookings() {
        List<Booking> bookings = bookingService.getAllUserBooking(booker.getId(), State.ALL, 0, 10);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.getFirst().getId());
    }

    @Test
    void testGetAllOwnerBookings() {
        List<Booking> ownerBookings = bookingService.getAllOwnerBooking(owner.getId(), State.ALL, 0, 10);

        assertNotNull(ownerBookings);
        assertEquals(1, ownerBookings.size());
        assertEquals(booking.getId(), ownerBookings.getFirst().getId());
    }

    @Test
    void testAddBookingItemNotAvailable() {
        item.setAvailable(false);
        itemRepository.save(item);

        RequestBookingDto requestBookingDto = RequestBookingDto.builder().build();
        requestBookingDto.setItemId(item.getId());
        requestBookingDto.setStart(LocalDateTime.now().plusDays(1));
        requestBookingDto.setEnd(LocalDateTime.now().plusDays(2));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.addBooking(booker.getId(), requestBookingDto));
        assertEquals("Item is not available", exception.getMessage());
    }

    @Test
    void testAddBookingByOwner() {
        RequestBookingDto requestBookingDto = RequestBookingDto.builder().build();
        requestBookingDto.setItemId(item.getId());
        requestBookingDto.setStart(LocalDateTime.now().plusDays(1));
        requestBookingDto.setEnd(LocalDateTime.now().plusDays(2));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookingService.addBooking(owner.getId(), requestBookingDto));
        assertEquals("User is not the owner of this booking", exception.getMessage());
    }

    @Test
    void testHandleBookingApprovalAlreadyApproved() {
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.handleBookingApproval(owner.getId(), booking.getId(), true));
        assertEquals("The status has already been approved", exception.getMessage());
    }

    @Test
    void testGetUserBookingByIdNotFound() {
        long invalidBookingId = 999L;

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookingService.getUserBookingById(booker.getId(), invalidBookingId));
        assertEquals("Booking not found", exception.getMessage());
    }

    @Test
    void testHandleBookingApprovalNotOwner() {
        long otherUserId = 999L;

        AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                () -> bookingService.handleBookingApproval(otherUserId, booking.getId(), true));
        assertEquals("You are not allowed to approve this booking", exception.getMessage());
    }

    @Test
    void testGetAllUserBookingsNotFound() {
        long invalidUserId = 999L;

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookingService.getAllUserBooking(invalidUserId, State.ALL, 0, 10));
        assertEquals("User with id 999 not found", exception.getMessage());
    }
}