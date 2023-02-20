package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE)
@Slf4j
public class ItemController {

    final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") int userId,
                       @Valid @RequestBody ItemDto itemDto) {
        if (itemDto == null) {
            throw new BadRequestException("Пустой запрос");
        }
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userId);
        Item returnItem = itemService.add(item, userId);

        return ItemMapper.toItemDto(returnItem);
    }

    @PatchMapping("{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto update(@PathVariable int itemId,
                          @RequestHeader("X-Sharer-User-Id") int userId,
                          @RequestBody ItemDto itemDto) {
        if (itemDto == null) {
            throw new BadRequestException("Пустой запрос");
        }
        log.info("Запрос на обновление вещи");
        Item item = ItemMapper.toItem(itemDto);
        Item returnItem = itemService.update(item, itemId, userId);

        return ItemMapper.toItemDto(returnItem);
    }

    @GetMapping("{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto get(@PathVariable int itemId) {
        if (itemId <= 0) {
            throw new BadRequestException("Идентификатор должен быть положительным");
        }
        Item item = itemService.find(itemId);

        return ItemMapper.toItemDto(item);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> findAllByOwner(@RequestHeader("X-Sharer-User-Id") int userId) {
        if (userId <= 0) {
            throw new BadRequestException("Идентификатор должен быть положительным");
        }
        List<ItemDto> dto = new ArrayList<>();
        for (Item item : itemService.findAllByUser(userId)) {
            dto.add(ItemMapper.toItemDto(item));
        }

        return dto;
    }

    @GetMapping("search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getByReview(@RequestParam(required = false) String text) {
        List<ItemDto> dto = new ArrayList<>();
        for (Item item : itemService.findByReview(text)) {
            dto.add(ItemMapper.toItemDto(item));
        }

        return dto;
    }
}
