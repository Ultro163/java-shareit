package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;

import java.util.List;

public interface ItemService {

    List<ItemWithBookingsDto> getAllOwnerItems(long userId);

    ItemWithBookingsDto getItem(long itemId);

    List<ItemDto> getItemsByText(String text);

    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);
}