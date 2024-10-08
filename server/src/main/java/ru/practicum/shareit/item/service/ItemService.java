package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    List<ItemWithBookingsDto> getAllOwnerItems(long userId);

    ItemWithBookingsDto getItem(long userId, long itemId);

    List<Item> getItemsByText(long userId, String text);

    Item addItem(long userId, ItemDto itemDto);

    Comment addComment(long userId, long itemId, CommentDto commentDto);

    Item updateItem(long userId, long itemId, ItemDto itemDto);
}