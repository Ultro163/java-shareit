package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.erorr.exception.AccessDeniedException;
import ru.practicum.shareit.erorr.exception.EntityNotFoundException;
import ru.practicum.shareit.erorr.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userServiceImpl;
    private final BookingMapper bookingMapper;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto addBooking(long userId, RequestBookingDto requestBookingDto) {
        Booking booking = bookingMapper.mapToBooking(requestBookingDto);
        User booker = userServiceImpl.checkUserExist(userId);
        Item item = itemRepository.findById(requestBookingDto.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        if (!item.getAvailable()) {
            throw new ValidationException("Item is not available");
        }
        if (item.getOwner().getId() == userId) {
            throw new EntityNotFoundException("Owner is not the owner of this booking");
        }

        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        Booking saveBooking = bookingRepository.save(booking);
        booking.setId(saveBooking.getId());
        return bookingMapper.mapToBookingDto(booking);
    }

    @Override
    public BookingDto handleBookingApproval(long userId, long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new AccessDeniedException("You are not allowed to approve this booking");
        }
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new ValidationException("The status has already been approved");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking saveBooking = bookingRepository.save(booking);
        return bookingMapper.mapToBookingDto(saveBooking);
    }

    @Override
    public BookingDto getUserBookingById(long userId, long bookingId) {
        Booking booking = bookingRepository.getUserBookingById(bookingId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        return bookingMapper.mapToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllUserBooking(long userId, String state) {
        userServiceImpl.checkUserExist(userId);
        List<Booking> bookingList = switch (state.toLowerCase()) {
            case "waiting" -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            case "rejected" ->
                    bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
            case "current" -> bookingRepository.findByBookerIdAndCurrentTimeOrderByStartDesc(userId,
                    BookingStatus.APPROVED, LocalDateTime.now());
            case "all" -> bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
            case "past" -> bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId,
                    LocalDateTime.now());
            case "future" -> bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId,
                    LocalDateTime.now());
            default -> throw new ValidationException("Unknown state: " + state);
        };

        return bookingList.stream().map(bookingMapper::mapToBookingDto).toList();
    }

    @Override
    public List<BookingDto> getAllOwnerBooking(long ownerId, String state) {
       userServiceImpl.checkUserExist(ownerId);
        List<Booking> bookingList = switch (state.toLowerCase()) {
            case "waiting" -> bookingRepository.findByOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);
            case "rejected" ->
                    bookingRepository.findByOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
            case "current" -> bookingRepository.findByOwnerIdAndCurrentTimeOrderByStartDesc(ownerId,
                    BookingStatus.APPROVED, LocalDateTime.now());
            case "all" -> bookingRepository.findAllByOwnerIdOrderByStartDesc(ownerId);
            case "past" -> bookingRepository.findAllByOwnerIdAndEndBeforeOrderByStartDesc(ownerId,
                    LocalDateTime.now());
            case "future" -> bookingRepository.findAllByOwnerIdAndStartAfterOrderByStartDesc(ownerId,
                    LocalDateTime.now());
            default -> throw new ValidationException("Unknown state: " + state);
        };

        return bookingList.stream().map(bookingMapper::mapToBookingDto).toList();
    }

    public BookingDtoForItem getBookingForItem(long itemId, String booking) {
        Booking bookingForItem = switch (booking) {
            case "last" -> bookingRepository.findLastBookingForItem(itemId, LocalDateTime.now()).orElse(null);
            case "next" -> bookingRepository.findNextBookingForItem(itemId, LocalDateTime.now()).orElse(null);
            default -> null;
        };
        return bookingMapper.mapToBookingDtoForItem(bookingForItem);
    }
}