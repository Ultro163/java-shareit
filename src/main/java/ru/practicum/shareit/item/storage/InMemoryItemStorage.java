package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.erorr.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public List<Item> getAllOwnerItems(long userId) {
        log.info("Get all items from user {}", userId);
        return items.values().stream().filter(item -> item.getOwner() == userId).toList();
    }

    @Override
    public List<Item> getAllItems() {
        log.info("Get all items");
        return new ArrayList<>(items.values());
    }

    @Override
    public Item getItem(long itemId) {
        log.info("Getting item with id {}", itemId);
        if (!items.containsKey(itemId)) {
            throw new EntityNotFoundException("User not found");
        }
        return items.get(itemId);
    }

    @Override
    public Item addItem(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        log.info("Added new item: {}", item);
        return item;
    }

    @Override
    public Item updateItem(Item updateItem) {
        Item item = items.get(updateItem.getId());
        Optional<Item> itemDtoOptional = Optional.of(updateItem);
        itemDtoOptional.map(Item::getName).ifPresent(item::setName);
        itemDtoOptional.map(Item::getDescription).ifPresent(item::setDescription);
        itemDtoOptional.map(Item::getAvailable).ifPresent(item::setAvailable);
        items.put(item.getId(), item);
        log.info("Updated item: {}", item);
        return item;
    }

    private Long getNextId() {
        long currentId = items.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentId;
    }
}