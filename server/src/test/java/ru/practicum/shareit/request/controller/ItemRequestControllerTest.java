package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ItemRequestController.class, ItemRequestMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper mapper;
    @MockBean
    private ItemRequestService itemRequestService;

    private final ItemRequestDto dto = ItemRequestDto.builder()
            .description("test")
            .build();

    @Test
    @SneakyThrows
    public void getRequestByIdWithItems() {
        mockMvc.perform(get("/requests/{1}", "1")
                        .header("X-Sharer-User-Id", "0"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemRequestService, times(1)).getRequestByIdWithItems(0L, 1L);
    }

    @Test
    @SneakyThrows
    public void getAllRequests() {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "0"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemRequestService, times(1)).getAllRequests(0L, null, null);
    }

    @Test
    @SneakyThrows
    public void getUserRequests() {
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "0"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemRequestService, times(1)).getUserRequests(0L, null, null);
    }

    @Test
    @SneakyThrows
    public void create() {

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "0")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemRequestService, times(1)).create(0L, dto);
    }
}