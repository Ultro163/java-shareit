package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemClient;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ItemController.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper mapper;
    @MockBean
    private ItemClient itemClient;

    private final ItemDto dto = ItemDto.builder()
            .id(1L)
            .name("Test")
            .description("test")
            .available(false)
            .requestId(1L)
            .build();

    private final CommentDto commentDto = CommentDto.builder()
            .id(1L)
            .text("test")
            .itemId(1L)
            .authorName("Test")
            .build();

    @Test
    @SneakyThrows
    public void getAllOwnerItemsWithStatusOk() {
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", "0"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemClient, times(1)).getAllOwnerItems(0L);
    }

    @Test
    @SneakyThrows
    public void getItemWithValidDataWithStatusOk() {
        mockMvc.perform(get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", "0"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemClient, times(1)).getItem(0L, 1L);
    }

    @Test
    @SneakyThrows
    public void getItemsByTextWithValidDataWithStatusOk() {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", "0")
                        .param("text", "test"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemClient, times(1)).getItemsByText(0L, "test");
    }

    @Test
    @SneakyThrows
    public void getItemsByEmptyTextWithStatusOk() {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", "0")
                        .param("text", ""))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemClient, times(1)).getItemsByText(0L, "");
    }

    @Test
    @SneakyThrows
    public void addItemWithValidDataWithStatusOk() {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "0")
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemClient, times(1)).addItem(0L, dto);
    }

    @Test
    @SneakyThrows
    public void addItemWithoutNameWithStatusBadRequest() {
        ItemDto noNameDto = ItemDto.builder()
                .id(1L)
                .description("test")
                .available(true)
                .requestId(1L)
                .build();

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "0")
                        .content(mapper.writeValueAsString(noNameDto))
                        .characterEncoding(UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemClient);
    }

    @Test
    @SneakyThrows
    public void addCommentWithValidDataWithStatusOk() {
        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", "0")
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemClient, times(1)).addComment(0L, 1L, commentDto);
    }

    @Test
    @SneakyThrows
    public void addCommentWithoutTextWithStatusBadRequest() {
        CommentDto noTextComment = CommentDto.builder()
                .id(1L)
                .text(null)
                .itemId(1L)
                .authorName("Test")
                .build();

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", "0")
                        .content(mapper.writeValueAsString(noTextComment))
                        .characterEncoding(UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemClient);
    }

    @Test
    @SneakyThrows
    public void updateItemWithValidDataWithStatusOk() {
        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", "0")
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemClient, times(1)).updateItem(0L, 1L, dto);
    }
}