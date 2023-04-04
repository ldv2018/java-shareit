package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserMessageDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody UserMessageDto dto) {
        log.info("Добавление пользователя {}", dto);
        if (dto == null) {
            throw new BadRequestException("Пустой запрос");
        }
        return userClient.postUser(dto);
    }

    @PatchMapping(value = "/{userId}")
    public ResponseEntity<Object> patch(@RequestBody UserMessageDto dto,
                                        @PathVariable @NotNull int userId) {
        log.info("Обновление пользователя {}, userId={}", dto, userId);
        if (dto == null) {
            throw new BadRequestException("Пустой запрос");
        }
        return userClient.patchUser(dto, userId);
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<Object> get(@PathVariable @NotNull int userId) {
        log.info("Запрос пользователя userId={}", userId);
        return userClient.getUser(userId);
    }

    @DeleteMapping(value = "/{userId}")
    public void delete(@PathVariable @NotNull int userId) {
        log.info("Удаление пользователя userId={}", userId);
        userClient.deleteUser(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Запрос всех пользователей");
        return userClient.getUsers();
    }
}
