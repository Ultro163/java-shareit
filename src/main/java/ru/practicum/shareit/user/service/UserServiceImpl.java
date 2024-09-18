package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.erorr.exception.EntityNotFoundException;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User getUser(long userId) {
        log.info("Get user with id {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));
    }

    @Override
    public User createUser(UserDto userDto) {
        log.info("Adding user {}", userDto);
        User user = userRepository.save(userMapper.mapToUser(userDto));
        log.info("User saved {}", user);
        return user;
    }

    @Override
    public User updateUser(long userId, UserDto userDto) {
        log.info("Updating user {}", userDto);
        User user = getUser(userId);
        Optional.ofNullable(userDto.getName()).ifPresent(user::setName);
        Optional.ofNullable(userDto.getEmail()).ifPresent(user::setEmail);
        user = userRepository.save(user);
        log.info("User updated {}", user);
        return user;
    }

    @Override
    public void deleteUser(long userId) {
        log.info("Deleting user with id {}", userId);
        userRepository.deleteById(userId);
        log.info("User deleted");
    }
}