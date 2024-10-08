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
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.mapper.CommentMapper;
import ru.practicum.shareit.mapper.ItemMapper;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ItemController.class, ItemMapper.class, CommentMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper mapper;
    @MockBean
    private ItemService itemServiceImpl;

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
    public void getAllOwnerItems() {
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", "0"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemServiceImpl, times(1)).getAllOwnerItems(0L);
    }

    @Test
    @SneakyThrows
    public void getItem() {
        mockMvc.perform(get("/items/{1}", "1")
                        .header("X-Sharer-User-Id", "0"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemServiceImpl, times(1)).getItem(0L, 1L);
    }

    @Test
    @SneakyThrows
    public void getItemsByText() {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", "0")
                        .param("text", ""))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemServiceImpl, times(1)).getItemsByText(0L, "");
    }

    @Test
    @SneakyThrows
    public void addItem() {

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "0")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemServiceImpl, times(1)).addItem(0L, dto);
    }

    @Test
    @SneakyThrows
    public void addComment() {

        mockMvc.perform(post("/items/{1}/comment", "1")
                        .header("X-Sharer-User-Id", "0")
                        .content(mapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemServiceImpl, times(1)).addComment(0L, 1L, commentDto);
    }

    @Test
    @SneakyThrows
    public void updateItem() {

        mockMvc.perform(patch("/items/{1}", "1")
                        .header("X-Sharer-User-Id", "0")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemServiceImpl, times(1)).updateItem(0L, 1L, dto);
    }
}