package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDto {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Boolean available;
    private String request;
}