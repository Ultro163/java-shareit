package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {

    User getUser(long userId);

    User createUser(UserDto userDto);

    User updateUser(long userId, UserDto userDto);

    void deleteUser(long userId);
}