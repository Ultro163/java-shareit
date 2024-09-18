package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.erorr.exception.EntityNotFoundException;
import ru.practicum.shareit.erorr.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userServiceImpl;
    private final ItemMapper itemMapper;
    private final BookingService bookingServiceImpl;

    @Override
    public List<ItemWithBookingsDto> getAllOwnerItems(long userId) {
        log.info("Get all items from user {}", userId);
        checkOwnerExist(userId);
        return itemRepository.findByOwnerId(userId)
                .stream()
                .map(itemMapper::mapToItemWithBookingsDto)
                .peek(itemWithBookingsDto -> itemWithBookingsDto.setLastBooking(bookingServiceImpl
                        .getBookingForItem(itemWithBookingsDto.getId(), "last")))
                .peek(itemWithBookingsDto -> itemWithBookingsDto.setNextBooking(bookingServiceImpl
                        .getBookingForItem(itemWithBookingsDto.getId(), "next")))
                .toList();
    }

    @Override
    public ItemWithBookingsDto getItem(long itemId) {
        log.info("Get item with id {}", itemId);
        return itemMapper.mapToItemWithBookingsDto(itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found")));

//        log.info("Get item with id {}", itemId);
//        ItemWithBookingsDto item = itemMapper.mapToItemWithBookingsDto(itemRepository.findById(itemId)
//                .orElseThrow(() -> new EntityNotFoundException("Item not found")));
//        item.setLastBooking(bookingServiceImpl.getBookingForItem(itemId, "last"));
//        item.setNextBooking(bookingServiceImpl.getBookingForItem(itemId, "next"));
//        return item;
    }

    @Override
    public List<ItemDto> getItemsByText(String text) {
        log.info("Getting items by text {}", text);
        if (text == null || text.isEmpty()) {
            return emptyList();
        }
        return itemRepository.searchItemsWithTextFilter(text.trim().toLowerCase())
                .stream()
                .map(itemMapper::mapToItemDto).toList();
    }

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        log.info("Adding item {}", itemDto);
        User owner = checkOwnerExist(userId);
        Item item = itemMapper.mapToItem(itemDto);
        item.setOwner(owner);
        Item saveItem = itemRepository.save(item);
        log.info("Item saved {}", saveItem);
        return itemMapper.mapToItemDto(saveItem);
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
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
        return itemMapper.mapToItemDto(saveItem);
    }

    private User checkOwnerExist(long userId) {
        return userServiceImpl.checkUserExist(userId);
    }
}