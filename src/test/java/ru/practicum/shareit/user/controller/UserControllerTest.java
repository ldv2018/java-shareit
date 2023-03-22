package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    void addTest() throws Exception {
        UserDto userDto = new UserDto(1,"Name", "test@test.ru");
        UserDto userResponseDto = new UserDto(1, "Name", "test@test.ru");
        User user = new User(1, "Name", "test@test.ru");
        Mockito.when(userService.add(Mockito.any(User.class)))
                .thenReturn(user);
        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.name", Matchers.is(userResponseDto.getName()), String.class))
                .andExpect(jsonPath("$.email", Matchers.is(userResponseDto.getEmail()), String.class));
        Mockito.verify(userService, Mockito.times(1))
                .add(Mockito.any(User.class));

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(null))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void patchTest() throws Exception {
        UserDto userMessageDto = new UserDto(1, "NameTest2", "test@test.ru2");
        UserDto userResponseDto = new UserDto(2, "NameTest2", "test@test.ru2");
        User user = new User(1, "NameTest2", "test@test.ru2");
        Mockito.when(userService.update(Mockito.any(User.class)))
                .thenReturn(user);
        mockMvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userMessageDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is(userResponseDto.getName()), String.class))
                .andExpect(jsonPath("$.email", Matchers.is(userResponseDto.getEmail()), String.class));
        Mockito.verify(userService, Mockito.times(1))
                .update(Mockito.any(User.class));
    }

    @Test
    void getTest() throws Exception {
        UserDto userMessageDto = new UserDto(1, "NameTest", "test@test.ru");
        UserDto userResponseDto = new UserDto(1, "NameTest", "test@test.ru");
        User user = new User(1, "NameTest", "test@test.ru");
        Mockito.when(userService.get(Mockito.anyInt()))
                .thenReturn(user);
        mockMvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is(userResponseDto.getName()), String.class))
                .andExpect(jsonPath("$.email", Matchers.is(userResponseDto.getEmail()), String.class));
        Mockito.verify(userService, Mockito.times(1))
                .get(Mockito.anyInt());
    }

    @Test
    void getAllTest() throws Exception {
        UserDto userMessageDto = new UserDto(1, "NameTest", "test@test.ru");
        UserDto userMessageDto2 = new UserDto(2, "NameTest2", "test@test.ru2");
        UserDto userResponseDto = new UserDto(1, "NameTest", "test@test.ru");
        UserDto userResponseDto2 = new UserDto(2, "NameTest2", "test@test.ru2");
        User user = new User(1, "NameTest", "test@test.ru");
        User user2 = new User(2, "NameTest2", "test@test.ru2");
        List<User> users = List.of(user, user2);
        Mockito.when(userService.get())
                .thenReturn(users);
        mockMvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[1].name", Matchers.is(userResponseDto2.getName()), String.class))
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
        Mockito.verify(userService, Mockito.times(1))
                .get();
    }

    @Test
    void deleteTest() throws Exception {
        Mockito.doNothing().when(userService).delete(Mockito.anyInt());
        mockMvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(userService, Mockito.times(1))
                .delete(Mockito.anyInt());
    }
}