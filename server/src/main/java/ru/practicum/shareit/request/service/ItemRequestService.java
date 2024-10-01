package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

public interface ItemRequestService {

    ItemRequest create(long userId, ItemRequestDto dto);
}
