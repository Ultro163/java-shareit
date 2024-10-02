package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ItemRequestDto {
    private Long id;
    @NotNull
    private String description;
    private LocalDateTime created;
    @Builder.Default
    List<ItemDto> items = new ArrayList<>();
}