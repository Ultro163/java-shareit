package ru.practicum.shareit.mapper;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

public class ItemRequestMapper {

    public static ItemRequestDto mapToItemRequestDto(ItemRequest ItemRequest) {
        return ItemRequestDto.builder()
                .id(ItemRequest.getId())
                .description(ItemRequest.getDescription())
                .build();
    }
}