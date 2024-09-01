package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> getAllOwnerItems(long userId);

    ItemDto getItem(long itemId);

    List<ItemDto> getItemsByText(String text);

    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);
}