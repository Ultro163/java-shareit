package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.util.annotations.ValidDateRange;

import java.time.LocalDateTime;

@Data
@Builder
@ValidDateRange
public class RequestBookingDto {
    @NotNull
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}