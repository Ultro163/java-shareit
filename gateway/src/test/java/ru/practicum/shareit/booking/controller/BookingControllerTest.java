package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.service.BookingClient;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    public void getAllOwnerBookingsWithValidUserId() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(bookingClient, times(1)).getAllOwnerBooking(1L, State.ALL, 0, 100);
    }

    @Test
    public void getAllOwnerBookingsWithoutUserIdWithBadRequest() throws Exception {
        mockMvc.perform(get("/bookings/owner"))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verifyNoInteractions(bookingClient);
    }

    @Test
    public void getAllUserBookingsWithValidUserId() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(bookingClient, times(1)).getAllUserBooking(1L, State.ALL, 0, 100);
    }

    @Test
    public void getAllUserBookingsWithUnknownStateWithBadRequest() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "UNKNOWN_STATUS"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Unknown state: UNKNOWN_STATUS")))
                .andDo(print());

        verifyNoInteractions(bookingClient);
    }

    @Test
    public void getAllOwnerBookingsWithUnknownStateWithBadRequest() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "UNKNOWN_STATUS"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Unknown state: UNKNOWN_STATUS")))
                .andDo(print());

        verifyNoInteractions(bookingClient);
    }

    @Test
    public void getUserBookingByIdWithValidUserId() throws Exception {
        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(bookingClient, times(1)).getUserBookingById(1L, 1L);
    }

    @Test
    public void getUserBookingByIdWithoutUserIdWithBadRequest() throws Exception {
        mockMvc.perform(get("/bookings/{bookingId}", 1L))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verifyNoInteractions(bookingClient);
    }

    @Test
    public void approveBookingWithValidRequest() throws Exception {
        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", "1")
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(bookingClient, times(1)).handleBookingApproval(1L, 1L, true);
    }

    @Test
    public void approveBookingWithoutUserIdWithBadRequest() throws Exception {
        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .param("approved", "true"))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verifyNoInteractions(bookingClient);
    }

    @Test
    public void createBookingWithValidData() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(bookingClient, times(1)).addBooking(1L, dto);
    }

    @Test
    public void createBookingWithoutUserIdWithBadRequest() throws Exception {
        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verifyNoInteractions(bookingClient);
    }

    @Test
    public void createBookingWithInvalidStartData() throws Exception {
        final RequestBookingDto invalidStartDto = RequestBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2021, 1, 7, 0, 0, 0))
                .end(LocalDateTime.of(2027, 1, 7, 0, 0, 0))
                .build();

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(invalidStartDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",
                        containsString("In the field 'start': Start date cannot be in the past")))
                .andDo(print());

        verifyNoInteractions(bookingClient);
    }

    @Test
    public void createBookingWithInvalidEndData() throws Exception {
        final RequestBookingDto invalidStartDto = RequestBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2026, 1, 7, 0, 0, 0))
                .end(LocalDateTime.of(2021, 1, 7, 0, 0, 0))
                .build();

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(invalidStartDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",
                        containsString("In the field 'end': End date cannot be in the past")))
                .andDo(print());

        verifyNoInteractions(bookingClient);
    }

    @Test
    public void createBookingStartAndEndDataEquals() throws Exception {
        final RequestBookingDto invalidStartDto = RequestBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2026, 1, 7, 0, 0, 0))
                .end(LocalDateTime.of(2026, 1, 7, 0, 0, 0))
                .build();

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(invalidStartDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",
                        containsString("In the field 'start': Start date cannot be the same as the end date")))
                .andDo(print());

        verifyNoInteractions(bookingClient);
    }

    @Test
    public void createBookingStartIsBeforeEndDataEquals() throws Exception {
        final RequestBookingDto invalidStartDto = RequestBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2027, 1, 7, 0, 0, 0))
                .end(LocalDateTime.of(2026, 1, 7, 0, 0, 0))
                .build();

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(invalidStartDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",
                        containsString("In the field 'start': Start date cannot be after the end date")))
                .andDo(print());

        verifyNoInteractions(bookingClient);
    }

    @Test
    public void createBookingWithoutStartAndEndTime() throws Exception {
        final RequestBookingDto invalidStartDto = RequestBookingDto.builder()
                .itemId(1L)
                .build();

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(invalidStartDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",
                        containsString("In the field 'start, end': Start and end dates cannot be null")))
                .andDo(print());

        verifyNoInteractions(bookingClient);
    }
}
