package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.erorr.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.ItemMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Repository
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public List<ItemDto> getAllItems(long userId) {
        return items.values().stream().filter(item -> item.getOwner() == userId).map(ItemMapper::mapToItemDto).toList();
    }

    @Override
    public ItemDto getItem(long itemId) {
        log.info("Getting item with id {}", itemId);
        checkItemExist(itemId);
        return ItemMapper.mapToItemDto(items.get(itemId));
    }

    @Override
    public List<ItemDto> getItemsByText(String text) {
        log.info("Getting items by text {}", text);
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        Pattern pattern = Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE);
        return items.values().stream()
                .filter(item -> item.getAvailable() &&
                        (pattern.matcher(item.getName()).find() ||
                                pattern.matcher(item.getDescription()).find()))
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        log.info("Adding new item: {}", itemDto);
        itemDto.setId(getNextId());
        Item item = ItemMapper.mapToItem(itemDto);
        item.setOwner(userId);
        items.put(itemDto.getId(), item);
        log.info("Added new item: {}", itemDto);
        return itemDto;
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        log.info("Updating existing item: {}", itemDto);
        Item item = items.get(itemId);
        itemDto.setId(itemId);
        Optional<ItemDto> itemDtoOptional = Optional.of(itemDto);
        itemDtoOptional.map(ItemDto::getName).ifPresent(item::setName);
        itemDtoOptional.map(ItemDto::getDescription).ifPresent(item::setDescription);
        itemDtoOptional.map(ItemDto::getAvailable).ifPresent(item::setAvailable);
        items.put(itemId, item);
        log.info("Updated item: {}", itemDto);
        return itemDto;
    }

    private Long getNextId() {
        long currentId = items.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentId;
    }

    private void checkItemExist(long itemId) {
        if (!items.containsKey(itemId)) {
            throw new EntityNotFoundException("User not found");
        }
    }
}