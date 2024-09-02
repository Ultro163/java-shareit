package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    List<Item> getAllOwnerItems(long userId);

    List<Item> getAllItems();

    Item getItem(long itemId);

    Item addItem(Item item);

    Item updateItem(Item item);
}