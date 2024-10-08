package ru.practicum.shareit.erorr;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import ru.practicum.shareit.erorr.exception.AccessDeniedException;
import ru.practicum.shareit.erorr.exception.EntityNotFoundException;
import ru.practicum.shareit.erorr.exception.ValidationException;
import ru.practicum.shareit.erorr.model.ErrorResponse;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ErrorHandlerTest {

    private ErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new ErrorHandler();
    }

    @Test
    void handleNotFound() {
        EntityNotFoundException exception = new EntityNotFoundException("Entity not found");
        ErrorResponse response = errorHandler.handleNotFound(exception);

        assertEquals("Entity not found", response.error());
    }

    @Test
    void handleConflict() {
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Data conflict");
        ErrorResponse response = errorHandler.handleConflict(exception);

        assertEquals("Conflict: this value already exists in the database", response.error());
    }

    @Test
    void handleAccessDeniedException() {
        AccessDeniedException exception = new AccessDeniedException("Access denied");
        ErrorResponse response = errorHandler.handleAccessDeniedException(exception);

        assertEquals("Access denied", response.error());
    }

    @Test
    void handleBadRequest_ValidationException() {
        ValidationException exception = new ValidationException("Validation error");
        ErrorResponse response = errorHandler.handleBadRequest(exception);

        assertEquals("Validation error", response.error());
    }


    @Test
    void handleBadRequest_MethodArgumentNotValidException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        var fieldError = new org.springframework.validation.FieldError("objectName",
                "fieldName",
                "Invalid value");
        var bindingResult = mock(org.springframework.validation.BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
        when(exception.getBindingResult()).thenReturn(bindingResult);

        ErrorResponse response = errorHandler.handleBadRequest(exception);

        assertEquals("Error in the field 'fieldName': Invalid value", response.error());
    }

    @Test
    void handleBadRequest_MissingServletRequestParameterException() {
        MissingServletRequestParameterException exception =
                new MissingServletRequestParameterException("param", "String");

        ErrorResponse response = errorHandler.handleBadRequest(exception);

        assertEquals("Required parameter missing: param", response.error());
    }

    @Test
    void handleBadRequest_GenericException() {
        Exception exception = new Exception("Some generic error occurred");

        ErrorResponse response = errorHandler.handleBadRequest(exception);

        assertEquals("Some generic error occurred", response.error());
    }
}