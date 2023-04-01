package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestMessageDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

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
    public ItemRequestResponseDto add(@RequestHeader(user) int userId,
                                      @RequestBody ItemRequestMessageDto itemRequestMessageDto) {
        log.info("Получен запрос на добавление itemRequest от пользователя " + userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestMessageDto);
        ItemRequest ir = itemRequestService.add(itemRequest, userId);

        return ItemRequestMapper.toItemRequestResponseDto(ir);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestResponseDto> get(@RequestHeader(user) int userId) {
        log.info("Получен запрос на список itemRequest от пользователя " + userId);
        Map<ItemRequest, List<Item>> itemRequestAndItemAnswer = itemRequestService.get(userId);
        if (itemRequestAndItemAnswer.isEmpty()) {
            log.info("List<ItemRequest> от пользователя " + userId + " пустой");
            return new ArrayList<ItemRequestResponseDto>();
        }
        List<ItemRequest> itemRequests = itemRequestAndItemAnswer.keySet()
                .stream()
                .collect(Collectors.toList());
        List<ItemRequestResponseDto> response = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            ItemRequestResponseDto itemRequestResponseDto = ItemRequestMapper.toItemRequestResponseDto(itemRequest);
            List<ItemRequestResponseDto.Answer> answers = new ArrayList<>();
            for (Item item : itemRequestAndItemAnswer.get(itemRequest)) {
                answers.add(ItemRequestMapper.toAnswer(item));
            }
            itemRequestResponseDto.setItems(answers);
            response.add(itemRequestResponseDto);
        }
        log.info("список itemRequest для пользователя " + userId + " сформирован");

        return response;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestResponseDto> getAll(@RequestHeader(value = user) int userId,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "99") int size) {
        log.info("Получен запрос на список всех itemRequest");
        Map<ItemRequest, List<Item>> itemRequestsAndAnswers = itemRequestService.getAll(userId, from, size);
        List<ItemRequest> itemRequests = itemRequestsAndAnswers.keySet()
                .stream()
                .collect(Collectors.toList());
        List<ItemRequestResponseDto> response = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            ItemRequestResponseDto itemRequestResponseDto = ItemRequestMapper.toItemRequestResponseDto(itemRequest);
            List<ItemRequestResponseDto.Answer> answers = new ArrayList<>();
            for (Item item : itemRequestsAndAnswers.get(itemRequest)) {
                answers.add(ItemRequestMapper.toAnswer(item));
            }
            itemRequestResponseDto.setItems(answers);
            response.add(itemRequestResponseDto);
        }
        log.info("список itemRequest сформирован");

        return response;
    }

    @GetMapping("{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestResponseDto get(@RequestHeader(user) int userId,
                                      @PathVariable int requestId) {
        Map<ItemRequest, List<Item>> itemRequestAndItemAnswer = itemRequestService.get(userId, requestId);
        List<ItemRequest> itemRequests = itemRequestAndItemAnswer.keySet()
                .stream()
                .collect(Collectors.toList());
        ItemRequestResponseDto response = ItemRequestMapper.toItemRequestResponseDto(itemRequests.get(0));
        List<ItemRequestResponseDto.Answer> answers = new ArrayList<>();
        for (Item item : itemRequestAndItemAnswer.get(itemRequests.get(0))) {
            answers.add(ItemRequestMapper.toAnswer(item));
        }
        response.setItems(answers);

        return response;
    }
}
