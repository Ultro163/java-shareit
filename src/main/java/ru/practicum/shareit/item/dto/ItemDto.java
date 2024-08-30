package ru.practicum.shareit.item.dto;

import lombok.Builder;

/**
 * TODO Sprint add-controllers.
 */
@Builder
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
}
