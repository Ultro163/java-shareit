package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
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
        if (requestBookingDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("End date is after start date");
        }
        if (requestBookingDto.getStart().equals(requestBookingDto.getEnd())) {
            throw new ValidationException("Start date is after end date");
        }
        if (requestBookingDto.getEnd().isBefore(requestBookingDto.getStart())) {
            throw new ValidationException("End date is after start date");
        }
        if (requestBookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Start date is after end date");
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
            throw new EntityNotFoundException("You are not allowed to approve this booking");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
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
        List<Booking> bookingList;
        switch (state.toLowerCase()) {
            case "waiting":
                bookingList = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
                break;
            case "rejected":
                bookingList = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
                break;
            case "current":
                bookingList = bookingRepository.findByBookerIdAndCurrentTimeOrderByStartDesc(userId, BookingStatus.APPROVED, LocalDateTime.now());
                break;
            case "all":
                bookingList = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                break;
            case "past":
                bookingList = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case "future":
                bookingList = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                break;
            default:
                throw new ValidationException("Unknown state: " + state);

        }

        return bookingList.stream().map(bookingMapper::mapToBookingDto).toList();
    }

    @Override
    public BookingDto getAllOwnerBooking(long ownerId, String state) {
//       userServiceImpl.checkUserExist(ownerId);
//        List<Booking> bookingList;
//        switch (state.toLowerCase()) {
//            case "waiting":
//                bookingList = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
//                break;
//            case "rejected":
//                bookingList = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
//                break;
//            case "current":
//                bookingList = bookingRepository.findByBookerIdAndCurrentTime(userId, BookingStatus.APPROVED, LocalDateTime.now());
//                break;
//            case "all":
//                bookingList = bookingRepository.findAllByBookerId(userId);
//                break;
//            case "past":
//            case "future":
//            default:
//                throw new ValidationException("Invalid state");
//
//        }
        return null;
    }


}