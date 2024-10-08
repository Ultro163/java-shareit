package ru.practicum.shareit.booking.util.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.util.annotations.ValidDateRange;

import java.time.LocalDateTime;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, RequestBookingDto> {

    private static final LocalDateTime CURRENT_LOCAL_DATA_TIME = LocalDateTime.now();

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(RequestBookingDto bookingDto, ConstraintValidatorContext context) {
        if (bookingDto == null || bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Start and end dates cannot be null")
                    .addPropertyNode("start, end")
                    .addConstraintViolation();
            return false;
        }

        if (bookingDto.getEnd().isBefore(CURRENT_LOCAL_DATA_TIME)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("End date cannot be in the past")
                    .addPropertyNode("end")
                    .addConstraintViolation();
            return false;
        }

        if (bookingDto.getStart().isBefore(CURRENT_LOCAL_DATA_TIME)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Start date cannot be in the past")
                    .addPropertyNode("start")
                    .addConstraintViolation();
            return false;
        }

        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Start date cannot be after the end date")
                    .addPropertyNode("start")
                    .addConstraintViolation();
            return false;
        }

        if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Start date cannot be the same as the end date")
                    .addPropertyNode("start")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}