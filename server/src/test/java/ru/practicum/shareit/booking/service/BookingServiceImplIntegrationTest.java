package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.erorr.exception.AccessDeniedException;
import ru.practicum.shareit.erorr.exception.EntityNotFoundException;
import ru.practicum.shareit.erorr.exception.ValidationException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplIntegrationTest {

    private final BookingServiceImpl bookingService;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    private User booker;
    private User owner;
    private Item item;
    private Booking booking;
    private Comment comment;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        userRepository.deleteAll();
        itemRepository.deleteAll();

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

        comment = new Comment();
        comment.setItem(item);
        comment.setAuthor(booker);
        comment.setText("Test Comment Text");
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

    @Test
    void testGetAllUserBookings_Waiting() {
        List<Booking> bookings = bookingService.getAllUserBooking(booker.getId(), State.WAITING, 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(BookingStatus.WAITING, bookings.getFirst().getStatus());
    }

    @Test
    void testGetAllUserBookings_Rejected() {
        booking.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking);

        List<Booking> bookings = bookingService.getAllUserBooking(booker.getId(), State.REJECTED, 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(BookingStatus.REJECTED, bookings.getFirst().getStatus());
    }

    @Test
    void testGetAllUserBookings_Current() {
        booking.setStart(LocalDateTime.now().minusHours(1));
        booking.setEnd(LocalDateTime.now().plusHours(1));
        bookingRepository.save(booking);

        List<Booking> bookings = bookingService.getAllUserBooking(booker.getId(), State.CURRENT, 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.getFirst().getId());
    }

    @Test
    void testGetAllUserBookings_Past() {
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        bookingRepository.save(booking);

        List<Booking> bookings = bookingService.getAllUserBooking(booker.getId(), State.PAST, 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.getFirst().getId());
    }

    @Test
    void testGetAllUserBookings_Future() {
        List<Booking> bookings = bookingService.getAllUserBooking(booker.getId(), State.FUTURE, 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.getFirst().getId());
    }

    @Test
    void testGetAllUserBookings_All() {
        List<Booking> bookings = bookingService.getAllUserBooking(booker.getId(), State.ALL, 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
    }

    @Test
    void testGetAllOwnerBookings_Waiting() {
        List<Booking> bookings = bookingService.getAllOwnerBooking(owner.getId(), State.WAITING, 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(BookingStatus.WAITING, bookings.getFirst().getStatus());
    }

    @Test
    void testGetAllOwnerBookings_Rejected() {
        booking.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking);

        List<Booking> bookings = bookingService.getAllOwnerBooking(owner.getId(), State.REJECTED, 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(BookingStatus.REJECTED, bookings.getFirst().getStatus());
    }

    @Test
    void testGetAllOwnerBookings_Current() {
        booking.setStart(LocalDateTime.now().minusHours(1));
        booking.setEnd(LocalDateTime.now().plusHours(1));
        bookingRepository.save(booking);

        List<Booking> bookings = bookingService.getAllOwnerBooking(owner.getId(), State.CURRENT, 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.getFirst().getId());
    }

    @Test
    void testGetAllOwnerBookings_Past() {
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        bookingRepository.save(booking);

        List<Booking> bookings = bookingService.getAllOwnerBooking(owner.getId(), State.PAST, 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.getFirst().getId());
    }

    @Test
    void testGetAllOwnerBookings_Future() {
        List<Booking> bookings = bookingService.getAllOwnerBooking(owner.getId(), State.FUTURE, 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.getFirst().getId());
    }

    @Test
    void testGetAllOwnerBookings_All() {
        List<Booking> bookings = bookingService.getAllOwnerBooking(owner.getId(), State.ALL, 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
    }

    @Test
    @DirtiesContext
    void testGetLastBookingForItem() {
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        Booking booking1 = bookingRepository.save(booking);
        System.out.println(booking1);
        BookingDtoForItem result = bookingService.getBookingForItem(item.getId(), "last");

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
    }

    @Test
    @DirtiesContext
    void testGetNextBookingForItem() {
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        bookingRepository.save(booking);

        BookingDtoForItem result = bookingService.getBookingForItem(item.getId(), "next");

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
    }

    @Test
    void testGetBookingForItemInvalidType() {
        BookingDtoForItem result = bookingService.getBookingForItem(item.getId(), "INVALID_TYPE");

        assertNull(result);
    }

    @Test
    void testGetBookingsForItems() {
        bookingRepository.save(booking);

        List<Long> itemIds = List.of(item.getId());
        Map<Long, List<Booking>> bookingsMap = bookingService.getBookingsForItems(itemIds);

        assertNotNull(bookingsMap);
        assertEquals(1, bookingsMap.size());
        assertTrue(bookingsMap.containsKey(item.getId()));
        assertEquals(1, bookingsMap.get(item.getId()).size());
        assertEquals(booking.getId(), bookingsMap.get(item.getId()).getFirst().getId());
    }

    @Test
    @DirtiesContext
    void testGetBookingsForItemsNoBookings() {
        Item newItem = new Item();
        newItem.setId(45L);
        List<Long> itemIds = List.of(newItem.getId());
        Map<Long, List<Booking>> bookingsMap = bookingService.getBookingsForItems(itemIds);
        assertNotNull(bookingsMap);
        assertTrue(bookingsMap.isEmpty());
    }

    @Test
    @DirtiesContext
    void testGetBookingsForCommentSuccess() {
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        bookingRepository.save(booking);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);

        List<Booking> bookings = bookingService.getBookingsForComment(item.getId(), booker.getId());

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.getFirst().getId());
    }

    @Test
    void testGetBookingsForCommentNoBookings() {
        List<Booking> bookings = bookingService.getBookingsForComment(item.getId(), booker.getId());

        assertNotNull(bookings);
        assertTrue(bookings.isEmpty());
    }
}