package ru.practicum.shareit.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Mapper
public interface UserMapper {

    UserDto mapToUserDto(User user);

    User mapToUser(UserDto userDto);
}