package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.ItemRequest;

public interface ItemRequestService {
    ItemRequest getOne(Long id);

    ItemRequest create(ItemRequest itemRequest);
}
