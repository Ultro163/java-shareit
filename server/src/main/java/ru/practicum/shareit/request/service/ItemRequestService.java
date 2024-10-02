package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequest create(long userId, ItemRequestDto dto);

    List<ItemRequestDto> getUserRequests(long userId, Integer from, Integer size);

    List<ItemRequestDto> getAllRequests(long userId, Integer from, Integer size);

    ItemRequest getRequestById(long userId, long requestId);
}