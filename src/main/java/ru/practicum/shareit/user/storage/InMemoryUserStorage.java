package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.erorr.exception.EntityNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(long userId) {
        log.info("Getting user {}", userId);
        if (!users.containsKey(userId)) {
            throw new EntityNotFoundException("User not found");
        }
        return users.get(userId);
    }

    @Override
    public User createUser(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Created new user: {}", user);
        return user;
    }

    @Override
    public User updateUser(User updateUser) {
        User user = users.get(updateUser.getId());
        Optional<User> optionalUpdateUser = Optional.of(updateUser);
        optionalUpdateUser.map(User::getName).ifPresent(user::setName);
        optionalUpdateUser.map(User::getEmail).ifPresent(user::setEmail);
        users.put(user.getId(), user);
        log.info("Updated user: {}", updateUser);
        return user;
    }

    @Override
    public void deleteUser(long userId) {
        users.remove(userId);
        log.info("Deleted user: {}", userId);
    }

    private Long getNextId() {
        long currentId = users.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentId;
    }
}