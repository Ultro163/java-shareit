package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.erorr.exception.ValidationException;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static State fromString(String state) {
        try {
            return State.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Unknown state: " + state);
        }
    }
}