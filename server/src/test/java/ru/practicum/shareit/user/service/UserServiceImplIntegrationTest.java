package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.erorr.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplIntegrationTest {

    @Autowired
    private UserServiceImpl userService;

    private User createdUser;

    @BeforeEach
    void setUp() {
        UserDto userDto = UserDto.builder().build();
        userDto.setName("Test User");
        userDto.setEmail("testuser@example.com");
        createdUser = userService.createUser(userDto);
    }

    @Test
    void testCreateUser() {
        UserDto newUserDto = UserDto.builder().build();
        newUserDto.setName("New User");
        newUserDto.setEmail("newuser@example.com");

        User newUser = userService.createUser(newUserDto);
        assertNotNull(newUser.getId());
        assertEquals(newUserDto.getName(), newUser.getName());
        assertEquals(newUserDto.getEmail(), newUser.getEmail());
    }

    @Test
    void testGetUser() {
        User foundUser = userService.getUser(createdUser.getId());
        assertNotNull(foundUser);
        assertEquals(createdUser.getId(), foundUser.getId());
        assertEquals(createdUser.getName(), foundUser.getName());
        assertEquals(createdUser.getEmail(), foundUser.getEmail());
    }

    @Test
    void testGetUserNotFound() {
        long invalidUserId = 999L;
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.getUser(invalidUserId));
        assertEquals("User with id 999 not found", exception.getMessage());
    }

    @Test
    void testUpdateUser() {
        UserDto updatedUserDto = UserDto.builder().build();
        updatedUserDto.setName("Updated User");
        updatedUserDto.setEmail("updateduser@example.com");

        User updatedUser = userService.updateUser(createdUser.getId(), updatedUserDto);
        assertNotNull(updatedUser);
        assertEquals(updatedUserDto.getName(), updatedUser.getName());
        assertEquals(updatedUserDto.getEmail(), updatedUser.getEmail());
    }

    @Test
    void testDeleteUser() {
        long userId = createdUser.getId();
        userService.deleteUser(userId);

        assertThrows(EntityNotFoundException.class, () -> userService.getUser(userId));
    }
}