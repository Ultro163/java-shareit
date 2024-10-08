package ru.practicum.shareit.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

@Mapper
public interface ItemRequestMapper {

    ItemRequest mapToItemRequest(ItemRequestDto itemRequest);

    ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest);
}