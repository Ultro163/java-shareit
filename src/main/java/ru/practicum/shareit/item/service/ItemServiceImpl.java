package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.erorr.exception.EntityNotFoundException;
import ru.practicum.shareit.erorr.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.mapper.CommentMapper;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final UserService userServiceImpl;
    private final ItemMapper itemMapper;
    private final BookingService bookingServiceImpl;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;
    private static final String LAST_BOOKING = "last";
    private static final String NEXT_BOOKING = "next";

    @Override
    @Transactional(readOnly = true)
    public List<ItemWithBookingsDto> getAllOwnerItems(long userId) {
        List<ItemWithBookingsDto> items = itemRepository.findByOwnerIdOrderByIdAsc(userId)
                .stream()
                .map(itemMapper::mapToItemWithBookingsDto)
                .toList();

        List<Long> itemIds = items.stream()
                .map(ItemWithBookingsDto::getId)
                .collect(Collectors.toList());

        Map<Long, List<Booking>> bookingsByItemId = bookingServiceImpl.getBookingsForItems(itemIds);

        Map<Long, List<BookingDtoForItem>> bookingDtoByItemId = bookingsByItemId.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(bookingMapper::mapToBookingDtoForItem)
                                .collect(Collectors.toList())
                ));

        items.forEach(item -> {
            List<BookingDtoForItem> bookings = bookingDtoByItemId.get(item.getId());
            if (bookings == null) {
                item.setLastBooking(null);
                item.setNextBooking(null);
            } else {
                item.setLastBooking(bookings.stream()
                        .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                        .max(Comparator.comparing(BookingDtoForItem::getEnd))
                        .orElse(null));
                item.setNextBooking(bookings.stream()
                        .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .min(Comparator.comparing(BookingDtoForItem::getStart))
                        .orElse(null));
            }
        });
        return items;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemWithBookingsDto getItem(long userId, long itemId) {
        log.info("Get item with id {}", itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        ItemWithBookingsDto itemWithBookingsDto = itemMapper.mapToItemWithBookingsDto(item);
        if (item.getOwner().getId() == userId) {
            itemWithBookingsDto.setLastBooking(bookingServiceImpl.getBookingForItem(item.getId(), LAST_BOOKING));
            itemWithBookingsDto.setNextBooking(bookingServiceImpl.getBookingForItem(item.getId(), NEXT_BOOKING));
        }
        itemWithBookingsDto.setComments(commentRepository.findByItemId(itemId)
                .stream().map(commentMapper::mapToCommentDto).toList());
        return itemWithBookingsDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> getItemsByText(String text) {
        log.info("Getting items by text {}", text);
        if (text == null || text.isEmpty()) {
            return emptyList();
        }
        return itemRepository.searchItemsWithTextFilter(text);
    }

    @Override
    public Item addItem(long userId, ItemDto itemDto) {
        log.info("Adding item {}", itemDto);
        User owner = userServiceImpl.getUser(userId);
        Item item = itemMapper.mapToItem(itemDto);
        item.setOwner(owner);
        Item saveItem = itemRepository.save(item);
        log.info("Item saved {}", saveItem);
        return saveItem;
    }

    @Override
    public Comment addComment(long userId, long itemId, CommentDto commentDto) {
        log.info("Adding comment {}", commentDto);
        User user = userServiceImpl.getUser(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Item not found"));
        List<Booking> bookings = bookingServiceImpl.getBookingsForComment(itemId, userId);

        if (bookings.isEmpty()) {
            throw new ValidationException("No completed bookings found");
        }
        Comment comment = commentMapper.mapToComment(commentDto);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        Comment saveComment = commentRepository.save(comment);
        log.info("Comment saved {}", saveComment);
        return saveComment;
    }

    @Override
    public Item updateItem(long userId, long itemId, ItemDto itemDto) {
        log.info("Updating existing item: {}", itemDto);
        checkOwnerExist(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Item not found"));
        if (item.getOwner().getId() != userId) {
            throw new ValidationException("Owner is not have this item");
        }
        Optional.ofNullable(itemDto.getName()).ifPresent(item::setName);
        Optional.ofNullable(itemDto.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(itemDto.getAvailable()).ifPresent(item::setAvailable);
        Item saveItem = itemRepository.save(item);
        log.info("Item updated {}", saveItem);
        return saveItem;
    }

    private void checkOwnerExist(long userId) {
        userServiceImpl.getUser(userId);
    }
}