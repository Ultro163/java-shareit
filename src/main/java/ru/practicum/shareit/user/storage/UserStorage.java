package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserStorage {

    UserDto getUser(long userId);

    UserDto createUser(UserDto userDto);

    UserDto updateUser(long userId, UserDto userDto);

    void deleteUser(long userId);
}