package ru.practicum.shareit.booking.dto;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}