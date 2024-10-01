package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;

@RequiredArgsConstructor
@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;

    private final ItemRequestMapper itemRequestMapper;

    @Override
    public ItemRequest create(long userId, ItemRequestDto dto) {
        ItemRequest itemRequest = itemRequestMapper.mapToItemRequest(dto);
        itemRequest.setId(userId);
        ItemRequest resultItemRequest = itemRequestRepository.save(itemRequest);
        return resultItemRequest;
    }
}
