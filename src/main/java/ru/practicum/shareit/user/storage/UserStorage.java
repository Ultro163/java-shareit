package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getAllUsers();

    User getUser(long userId);

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(long userId);
}