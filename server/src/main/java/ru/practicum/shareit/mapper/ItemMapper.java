package ru.practicum.shareit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.model.Item;

@Mapper
public interface ItemMapper {

    @Mapping(target = "requestId", source = "request.id")
    ItemDto mapToItemDto(Item item);

    Item mapToItem(ItemDto itemDto);

    ItemWithBookingsDto mapToItemWithBookingsDto(Item item);
}