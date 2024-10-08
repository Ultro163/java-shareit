package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.mapper.BookingMapper;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({BookingController.class, BookingMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper mapper;
    @MockBean
    private BookingService bookingService;

    private final RequestBookingDto dto = RequestBookingDto.builder()
            .itemId(1L)
            .start(LocalDateTime.of(2025, 1, 7, 0, 0, 0))
            .end(LocalDateTime.of(2026, 1, 7, 0, 0, 0))
            .build();

    @Test
    @SneakyThrows
    public void addBooking() {

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
        verify(bookingService, times(1)).addBooking(1L, dto);
    }

    @Test
    @SneakyThrows
    public void handleBookingApproval() {
        mockMvc.perform(patch("/bookings/{1}", "1")
                        .header("X-Sharer-User-Id", "0")
                        .param("approved", "false"))
                .andExpect(status().isOk())
                .andDo(print());
        verify(bookingService, times(1)).handleBookingApproval(0L, 1L, false);
    }

    @Test
    @SneakyThrows
    public void getUserBookingById() {
        mockMvc.perform(get("/bookings/{1}", "1")
                        .header("X-Sharer-User-Id", "0"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(bookingService, times(1)).getUserBookingById(0L, 1L);
    }

    @Test
    @SneakyThrows
    public void getAllUserBooking() {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "0"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(bookingService, times(1)).getAllUserBooking(0L, State.ALL, null, null);
    }

    @Test
    @SneakyThrows
    public void getAllOwnerBooking() {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "0"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(bookingService, times(1)).getAllOwnerBooking(0L, State.ALL, null, null);
    }
}