package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UserController.class, UserMapper.class})
@AutoConfigureWebMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper mapper;

    private User user;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = UserDto.builder()
                .name("name1")
                .email("email1")
                .build();

        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("email");
    }

    @Test
    @SneakyThrows
    public void deleteUser() {
        mockMvc.perform(delete("/users/{0}", "0"))
                .andExpect(status().isOk())
                .andDo(print());
        verify(userService, times(1)).deleteUser(anyLong());
    }

    @Test
    @SneakyThrows
    public void updateUser() {
        when(userService.updateUser(1L, userDto)).thenReturn(user);

        mockMvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("name")))
                .andExpect(jsonPath("$.email", is("email")))
                .andDo(print());

        verify(userService, times(1)).updateUser(1L, userDto);
    }

    @Test
    @SneakyThrows
    public void createUser() {

        when(userService.createUser(any())).thenReturn(user);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("name")))
                .andExpect(jsonPath("$.email", is("email")))
                .andDo(print());

        verify(userService, times(1)).createUser(any());
    }

    @Test
    @SneakyThrows
    public void getUser() {

        when(userService.getUser(1L)).thenReturn(user);

        mockMvc.perform(get("/users/{1}", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("name")))
                .andExpect(jsonPath("$.email", is("email")))
                .andDo(print());

        verify(userService, times(1)).getUser(1);
    }
}