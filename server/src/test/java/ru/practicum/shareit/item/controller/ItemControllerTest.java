package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.mapper.CommentMapper;
import ru.practicum.shareit.mapper.ItemMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ItemController.class, ItemMapper.class, CommentMapper.class})
@AutoConfigureWebMvc
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

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
    public void getAllOwnerItems() throws Exception {
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", "0"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getItem() throws Exception {
        mockMvc.perform(get("/items/{0}", "0")
                        .header("X-Sharer-User-Id", "0"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getItemsByText() throws Exception {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", "0")
                        .param("text", ""))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void addItem() throws Exception {

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "0")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void addComment() throws Exception {

        mockMvc.perform(post("/items/{0}/comment", "0")
                        .header("X-Sharer-User-Id", "0")
                        .content(mapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void updateItem() throws Exception {

        mockMvc.perform(patch("/items/{0}", "0")
                        .header("X-Sharer-User-Id", "0")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
