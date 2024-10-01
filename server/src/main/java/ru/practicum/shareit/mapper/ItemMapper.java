package ru.practicum.shareit.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.model.Item;

@Mapper
public interface ItemMapper {

    ItemDto mapToItemDto(Item item);

    Item mapToItem(ItemDto itemDto);

    ItemWithBookingsDto mapToItemWithBookingsDto(Item item);
}