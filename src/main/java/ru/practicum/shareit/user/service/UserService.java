package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

    UserDto getUser(long userId);

    UserDto createUser(UserDto userDto);

    UserDto updateUser(long userId, UserDto userDto);

    void deleteUser(long userId);
}