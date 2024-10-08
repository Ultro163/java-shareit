package ru.practicum.shareit.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.error.exception.ValidationException;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleBadRequest(final Exception e) {
        log.warn(e.getMessage());
        String errorMessage;

        if (e instanceof MethodArgumentNotValidException ex) {
            errorMessage = ex.getBindingResult().getFieldErrors().stream()
                    .map(fieldError -> String.format("In the field '%s': %s", fieldError.getField(),
                            fieldError.getDefaultMessage()))
                    .findFirst()
                    .orElse("Incorrect data");
            return new ResponseEntity<>(Map.of("error", errorMessage), HttpStatus.BAD_REQUEST);
        } else {
            errorMessage = e.getMessage();
            return new ResponseEntity<>(Map.of("error", errorMessage), HttpStatus.BAD_REQUEST);
        }
    }
}