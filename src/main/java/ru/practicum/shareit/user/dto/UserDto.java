package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public class UserDto {
    private Long id;
    @NotNull
    private String name;
    @Email
    private String email;
}
