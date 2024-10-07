package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestClient;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ItemRequestController.class})
public class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestClient itemRequestClient;

    @Autowired
    private ObjectMapper mapper;

    private final ItemRequestDto dto = ItemRequestDto.builder()
            .description("test")
            .build();

    @Test
    void createRequestWithoutDescriptionWithStatusBadRequest() throws Exception {
        ItemRequestDto noDescription = ItemRequestDto.builder()
                .description(null)
                .build();

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(noDescription))
                        .characterEncoding(UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemRequestClient);
    }

    @Test
    void createRequestWithEmptyDescriptionWithStatusBadRequest() throws Exception {
        ItemRequestDto emptyDescription = ItemRequestDto.builder()
                .description("")
                .build();

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(emptyDescription))
                        .characterEncoding(UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemRequestClient);
    }

    @Test
    void createRequestWithValidDataWithStatusOk() throws Exception {
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemRequestClient, times(1)).create(1L, dto);
    }

    @Test
    void getUserRequestsWithValidUserIdWithStatusOk() throws Exception {
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemRequestClient, times(1)).getUserRequests(1L, 0, 100);
    }

    @Test
    void getUserRequestsWithoutUserIdWithStatusBadRequest() throws Exception {
        mockMvc.perform(get("/requests"))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verifyNoInteractions(itemRequestClient);
    }


    @Test
    void getAllRequestsWithValidUserIdWithStatusOk() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "100"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemRequestClient, times(1)).getAllRequests(1L, 0, 100);
    }

    @Test
    void getAllRequestsWithoutUserIdWithStatusBadRequest() throws Exception {
        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verifyNoInteractions(itemRequestClient);
    }

    @Test
    void getRequestByIdWithValidDataWithStatusOk() throws Exception {
        mockMvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemRequestClient, times(1)).getRequestByIdWithItems(1L, 1L);
    }

    @Test
    void getRequestByIdWithoutUserIdWithStatusBadRequest() throws Exception {
        mockMvc.perform(get("/requests/{requestId}", 1L))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verifyNoInteractions(itemRequestClient);
    }

    @Test
    void getRequestByIdWithoutRequestIdWithStatusBadRequest() throws Exception {
        mockMvc.perform(get("/requests/" + null)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verifyNoInteractions(itemRequestClient);
    }
}