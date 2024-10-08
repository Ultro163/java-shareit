package ru.practicum.shareit.booking.util.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.practicum.shareit.booking.util.constraints.DateRangeValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
public @interface ValidDateRange {

    String message() default "{Booking date is not valid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}