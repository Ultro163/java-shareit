package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ItemRequestController.class, ItemRequestMapper.class})
public class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private ObjectMapper mapper;

    private final ItemRequestDto dto = ItemRequestDto.builder()
            .description("test")
            .build();

    @Test
    public void getRequestByIdWithItems() throws Exception {
        mockMvc.perform(get("/requests/{0}", "0")
                        .header("X-Sharer-User-Id", "0"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getAllRequests() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "0"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getUserRequests() throws Exception {
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "0"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void create() throws Exception {

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "0")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }


}
