package ru.practicum.shareit.erorr.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void error_ReturnsCorrectValue() {
        String errorMessage = "Test error message";
        ErrorResponse errorResponse = new ErrorResponse(errorMessage);

        String result = errorResponse.error();

        assertNotNull(result);
        assertEquals(errorMessage, result);
    }

    @Test
    void error_WithNullMessage() {
        ErrorResponse errorResponse = new ErrorResponse(null);

        String result = errorResponse.error();

        assertNull(result);
    }
}