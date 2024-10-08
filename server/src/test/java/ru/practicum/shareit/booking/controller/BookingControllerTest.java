package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper mapper;

    private final RequestBookingDto dto = RequestBookingDto.builder()
            .itemId(1L)
            .start(LocalDateTime.of(2025, 1, 7, 0, 0, 0))
            .end(LocalDateTime.of(2026, 1, 7, 0, 0, 0))
            .build();

    @Test
    public void addBooking() throws Exception {

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
        verify(bookingService, times(1)).addBooking(1L, dto);
    }

    @Test
    public void handleBookingApproval() throws Exception {
        mockMvc.perform(patch("/bookings/{1}", "1")
                        .header("X-Sharer-User-Id", "0")
                        .param("approved", "false"))
                .andExpect(status().isOk())
                .andDo(print());
        verify(bookingService, times(1)).handleBookingApproval(0L, 1L, false);
    }

    @Test
    public void getUserBookingById() throws Exception {
        mockMvc.perform(get("/bookings/{1}", "1")
                        .header("X-Sharer-User-Id", "0"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(bookingService, times(1)).getUserBookingById(0L, 1L);
    }

    @Test
    public void getAllUserBooking() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "0"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(bookingService, times(1)).getAllUserBooking(0L, State.ALL, null, null);
    }

    @Test
    public void getAllOwnerBooking() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "0"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(bookingService, times(1)).getAllOwnerBooking(0L, State.ALL, null, null);
    }
}