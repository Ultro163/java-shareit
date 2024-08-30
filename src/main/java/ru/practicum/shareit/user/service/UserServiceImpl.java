package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.UserStorage;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage inMemoryUserStorage;

    @Override
    public UserDto getUser(long userId) {
        return inMemoryUserStorage.getUser(userId);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        return inMemoryUserStorage.createUser(userDto);
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        return inMemoryUserStorage.updateUser(userId, userDto);
    }

    @Override
    public void deleteUser(long userId) {
        inMemoryUserStorage.deleteUser(userId);
    }
}