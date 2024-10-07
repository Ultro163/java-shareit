package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserClient;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UserController.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserClient userClient;

    @Autowired
    private ObjectMapper mapper;

    private final UserDto dto = UserDto.builder()
            .id(1L)
            .name("test")
            .email("test@test.ru")
            .build();

    @Test
    public void deleteUserWithValidIdWithStatusOk() throws Exception {
        mvc.perform(delete("/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andDo(print());

        verify(userClient, times(1)).deleteUser(1L);
    }

    @Test
    public void updateUserWithValidDataWithStatusOk() throws Exception {
        UserDto updateDto = UserDto.builder()
                .name("updatedName")
                .email("updated@test.ru")
                .build();

        mvc.perform(patch("/users/{userId}", 1L)
                        .content(mapper.writeValueAsString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8))
                .andExpect(status().isOk())
                .andDo(print());

        verify(userClient, times(1)).updateUser(1L, updateDto);
    }

    @Test
    public void createUserWithValidDataWithStatusOk() throws Exception {
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8))
                .andExpect(status().isOk())
                .andDo(print());

        verify(userClient, times(1)).createUser(dto);
    }

    @Test
    public void createUserWithoutNameWithStatusBadRequest() throws Exception {
        UserDto noNameDto = UserDto.builder()
                .email("test@test.ru")
                .build();

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(noNameDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userClient);
    }

    @Test
    public void createUserWithoutEmailWithStatusBadRequest() throws Exception {
        UserDto noEmailDto = UserDto.builder()
                .name("noEmail")
                .build();

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(noEmailDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userClient);
    }

    @Test
    public void getUserWithValidIdWithStatusOk() throws Exception {
        mvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andDo(print());

        verify(userClient, times(1)).getUser(1L);
    }
}