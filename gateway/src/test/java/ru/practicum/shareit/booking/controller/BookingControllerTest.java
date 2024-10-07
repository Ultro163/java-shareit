package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.service.BookingClient;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({BookingController.class})
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingClient bookingClient;

    @Autowired
    private ObjectMapper mapper;

    private final RequestBookingDto dto = RequestBookingDto.builder()
            .itemId(1L)
            .start(LocalDateTime.of(2025, 1, 7, 0, 0, 0))
            .end(LocalDateTime.of(2027, 1, 7, 0, 0, 0))
            .build();

    @Test
    public void getAllOwnerBooking() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "0"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getAllUserBooking() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "0"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getUserBookingById() throws Exception {
        mockMvc.perform(get("/bookings/{0}", "0")
                        .header("X-Sharer-User-Id", "0"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(bookingClient, times(1)).getUserBookingById(0L, 0L);
    }

    @Test
    public void handleBookingApproval() throws Exception {
        mockMvc.perform(patch("/bookings/{0}", "0")
                        .header("X-Sharer-User-Id", "0")
                        .param("approved", "false"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void addBooking() throws Exception {

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "0")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}