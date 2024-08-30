package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.erorr.exception.ConflictException;
import ru.practicum.shareit.erorr.exception.EntityNotFoundException;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public UserDto getUser(long userId) {
        log.info("Getting user {}", userId);
        checkUserExist(userId);
        return UserMapper.mapToUserDto(users.get(userId));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Creating new user: {}", userDto);
        userValidate(userDto);
        userDto.setId(getNextId());
        User user = UserMapper.mapToUser(userDto);
        users.put(user.getId(), user);
        log.info("Created new user: {}", user);
        return userDto;
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        log.info("Updating user: {}", userDto);
        checkUserExist(userId);
        userValidate(userDto);
        User user = users.get(userId);
        Optional<UserDto> optionalUserDto = Optional.of(userDto);
        optionalUserDto.map(UserDto::getName).ifPresent(user::setName);
        optionalUserDto.map(UserDto::getEmail).ifPresent(user::setEmail);
        users.put(userId, user);
        userDto.setId(userId);
        log.info("Updated user: {}", user);
        return userDto;
    }

    @Override
    public void deleteUser(long userId) {
        log.info("Deleting user: {}", userId);
        checkUserExist(userId);
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

    private void checkUserExist(long userId) {
        if (!users.containsKey(userId)) {
            throw new EntityNotFoundException("User not found");
        }
    }

    private void userValidate(UserDto userDto) {
        if (users.values().stream()
                .anyMatch(user -> user.getEmail() != null && user.getEmail().equals(userDto.getEmail()))) {
            throw new ConflictException("Email already exists");
        }
    }
}