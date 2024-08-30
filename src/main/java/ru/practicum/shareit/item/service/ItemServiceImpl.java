package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage inMemoryItemStorage;
    private final UserService inMemoryUserService;

    @Override
    public List<ItemDto> getAllItems(long userId) {
        checkOwner(userId);
        return inMemoryItemStorage.getAllItems(userId);
    }

    @Override
    public ItemDto getItem(long itemId) {
        return inMemoryItemStorage.getItem(itemId);
    }

    @Override
    public List<ItemDto> getItemsByText(String text) {
        return inMemoryItemStorage.getItemsByText(text);
    }

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        checkOwner(userId);
        return inMemoryItemStorage.addItem(userId, itemDto);
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        checkOwner(userId);
        return inMemoryItemStorage.updateItem(userId, itemId, itemDto);
    }

    private void checkOwner(long userId) {
        inMemoryUserService.getUser(userId);
    }
}