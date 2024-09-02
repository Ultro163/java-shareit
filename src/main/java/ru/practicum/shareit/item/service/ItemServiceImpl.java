package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage inMemoryItemStorage;
    private final UserService userServiceImpl;

    @Override
    public List<ItemDto> getAllOwnerItems(long userId) {
        checkOwner(userId);
        return inMemoryItemStorage.getAllOwnerItems(userId)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public ItemDto getItem(long itemId) {
        return ItemMapper.mapToItemDto(inMemoryItemStorage.getItem(itemId));
    }

    @Override
    public List<ItemDto> getItemsByText(String text) {
        log.info("Getting items by text {}", text);
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        Pattern pattern = Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE);
        return inMemoryItemStorage.getAllItems().stream()
                .filter(item -> item.getAvailable() &&
                        (pattern.matcher(item.getName()).find() ||
                                pattern.matcher(item.getDescription()).find()))
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        log.info("Adding new item: {}", itemDto);
        checkOwner(userId);
        Item item = ItemMapper.mapToItem(itemDto);
        item.setOwner(userId);
        return ItemMapper.mapToItemDto(inMemoryItemStorage.addItem(item));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        log.info("Updating existing item: {}", itemDto);
        checkOwner(userId);
        getItem(itemId);
        Item item = ItemMapper.mapToItem(itemDto);
        item.setOwner(userId);
        item.setId(itemId);
        return ItemMapper.mapToItemDto(inMemoryItemStorage.updateItem(item));
    }

    private void checkOwner(long userId) {
        userServiceImpl.getUser(userId);
    }
}