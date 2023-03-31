package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.ItemMessageDto;
import ru.practicum.shareit.item.dto.CommentMessageDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
@Slf4j
public class ItemController {

    private final ItemClient itemClient;

    static final String HEADER_REQUEST = "X-Sharer-User-Id";


    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody ItemMessageDto dto,
                                      @RequestHeader(HEADER_REQUEST) int userId) {
        log.info("Добавление вещи {}", dto);
        if (dto == null) {
            throw new BadRequestException("Пустой запрос");
        }
        return itemClient.postItem(dto, userId);
    }

    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<Object> patch(@RequestBody ItemMessageDto dto,
                                        @PathVariable @NotNull int itemId,
                                        @RequestHeader(HEADER_REQUEST) int userId) {
        log.info("Обновление вещи {}", dto);
        if (dto == null) {
            throw new BadRequestException("Пустой запрос");
        }
        return itemClient.patchItem(dto, userId, itemId);
    }

    @GetMapping(value = "/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable @NotNull int itemId,
                                          @RequestHeader(HEADER_REQUEST) int userId) {
        log.info("Запрос вещи {} от пользователя {}", itemId, userId);
        if (itemId <= 0) {
            throw new BadRequestException("Идентификатор должен быть положительным");
        }
        return itemClient.getItem(itemId, userId);
    }

/*    @DeleteMapping(value = "/{itemId}")
    public void deleteItem(@PathVariable @NotNull int itemId,
                           @RequestHeader(HEADER_REQUEST) int idUser) {
        log.info("Delete Item with {} user {}", itemId, idUser);
        itemClient.deleteItem(itemId, idUser);
    }*/

    @GetMapping
    public ResponseEntity<Object> getAllByOwner(@RequestHeader(HEADER_REQUEST) int userId,
                                                @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(name = "size", defaultValue = "99") @Positive int size) {
        log.info("Получение всех вещей от пользователя {}", userId);
        if (userId <= 0) {
            throw new BadRequestException("Идентификатор должен быть положительным");
        }
        if (from < 0 || size < 1) {
            log.info("Получены неверные значения size = " + size + ", from = " + from);
            throw new BadRequestException("Параметры пагинации должны быть >= 0");
        }
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Object> getByRewiev(@RequestParam String text,
                                              @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                              @RequestParam(name = "size", defaultValue = "99") @Positive int size) {
        log.info("Get Comment with search {} from {} size {}", text, from, size);
        if (text == null || text.isEmpty() || text.trim().isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        } else {
            return itemClient.searchItems(text, from, size);
        }
    }

    @PostMapping(value = "/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable @NotNull int itemId,
                                             @Valid @RequestBody CommentMessageDto dto,
                                             @RequestHeader(HEADER_REQUEST) int userId) {
        log.info("Post Comment with dto {} itemId {} idUser {}", dto, itemId, userId);
        return itemClient.saveComment(dto, userId, itemId);
    }
}
