package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestMessageDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Validated
@Slf4j
public class ItemRequestController {

    static final String HEADER_REQUEST = "X-Sharer-User-Id";

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody ItemRequestMessageDto dto,
                                      @RequestHeader(HEADER_REQUEST) int idUser) {
        log.info("Добавление запроса на вещь {} от пользователя {} ", dto, idUser);
        if (dto == null) {
            log.info("Пустое тело запроса");
            throw new BadRequestException("Пустой запрос");
        }
        return itemRequestClient.postRequest(dto, idUser);
    }

    @GetMapping
    public ResponseEntity<Object> get(@RequestHeader(HEADER_REQUEST) int idUser) {
        log.info("Получение своих запросов пользователя {} ", idUser);
        return itemRequestClient.getMyRequest(idUser);
    }

    @GetMapping(value = "/{requestId}")
    public ResponseEntity<Object> get(@PathVariable @NotNull int requestId,
                                      @RequestHeader(HEADER_REQUEST) int idUser) {
        log.info("Получение запроса, пользователь {} запрос {} ", idUser, requestId);
        return itemRequestClient.getRequest(idUser, requestId);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Object> getAll(@RequestHeader(HEADER_REQUEST) int idUser,
                                         @RequestParam(name = "from", defaultValue = "0") int from,
                                         @RequestParam(name = "size", defaultValue = "99") int size) {
        log.info("Поучение всех запросов для пользователя {}  from {} size {}", idUser, from, size);
        if (from < 0 || size < 1) {
            log.info("Получены неверные значения size = " + size + ", from = " + from);
            throw new BadRequestException("Параметры пагинации должны быть >= 0");
        }
        return itemRequestClient.getAll(idUser, from, size);
    }
}