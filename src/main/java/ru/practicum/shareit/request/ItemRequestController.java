package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Slf4j
public class ItemRequestController {
    final ItemRequestService itemRequestService;
    final String user = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto add(@RequestHeader(user) int userId,
                              @Valid @RequestBody ItemRequest itemRequest) {
        log.info("Получен запрос на добавление itemRequest от пользователя " + userId);
        if (itemRequest == null) {
            log.info("Пустое тело запроса");
            throw new BadRequestException("Пустой запрос");
        }
        ItemRequest ir = itemRequestService.add(itemRequest, userId);

        return ItemRequestMapper.toItemRequestDto(ir);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> get(@RequestHeader(user) int userId) {
        log.info("Получен запрос на список itemRequest от пользователя " + userId);
        Map<ItemRequest, List<Item>> itemRequestAndItemAnswer = itemRequestService.get(userId);
        if (itemRequestAndItemAnswer.isEmpty()) {
            log.info("List<ItemRequest> от пользователя " + userId + " пустой");
            return new ArrayList<ItemRequestDto>();
        }
        List<ItemRequest> itemRequests = itemRequestAndItemAnswer.keySet()
                .stream()
                .collect(Collectors.toList());
        List<ItemRequestDto> response = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
            List<ItemRequestDto.Answer> answers = new ArrayList<>();
            for (Item item : itemRequestAndItemAnswer.get(itemRequest)) {
                answers.add(ItemRequestMapper.toAnswer(item));
            }
            itemRequestDto.setItems(answers);
            response.add(itemRequestDto);
        }
        log.info("список itemRequest для пользователя " + userId + " сформирован");

        return response;
    }

    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> getAll(@RequestHeader(value = user) int userId,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "99") int size) {
        log.info("Получен запрос на список всех itemRequest");
        if (from < 0 || size < 1) {
            log.info("Получены неверные значения size = " + size + ", from = " + from);
            throw new BadRequestException("Параметры пагинации должны быть >= 0");
        }
        Map<ItemRequest, List<Item>> itemRequestsAndAnswers = itemRequestService.getAll(userId, from, size);
        List<ItemRequest> itemRequests = itemRequestsAndAnswers.keySet()
                .stream()
                .collect(Collectors.toList());
        List<ItemRequestDto> response = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
            List<ItemRequestDto.Answer> answers = new ArrayList<>();
            for (Item item : itemRequestsAndAnswers.get(itemRequest)) {
                answers.add(ItemRequestMapper.toAnswer(item));
            }
            itemRequestDto.setItems(answers);
            response.add(itemRequestDto);
        }
        log.info("список itemRequest сформирован");

        return response;
    }

    @GetMapping("{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto get(@RequestHeader(user) int userId,
                              @PathVariable int requestId) {
        Map<ItemRequest, List<Item>> itemRequestAndItemAnswer = itemRequestService.get(userId, requestId);
        List<ItemRequest> itemRequests = itemRequestAndItemAnswer.keySet()
                .stream()
                .collect(Collectors.toList());
        ItemRequestDto response = ItemRequestMapper.toItemRequestDto(itemRequests.get(0));
        List<ItemRequestDto.Answer> answers = new ArrayList<>();
        for (Item item : itemRequestAndItemAnswer.get(itemRequests.get(0))) {
            answers.add(ItemRequestMapper.toAnswer(item));
        }
        response.setItems(answers);

        return response;
    }
}
