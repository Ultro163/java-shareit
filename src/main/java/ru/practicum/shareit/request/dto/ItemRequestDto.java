package ru.practicum.shareit.request.dto;

import lombok.Builder;

/**
 * TODO Sprint add-item-requests.
 */
@Builder
public class ItemRequestDto {
    private Long id;
    private String description;
}
