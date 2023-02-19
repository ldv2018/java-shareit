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

        return ItemMapper.toItemDto(itemService.add(item, userId));
    }

    /*
    Редактирование вещи. Эндпойнт PATCH /items/{itemId}.
    Изменить можно название, описание и статус доступа к аренде.
    Редактировать вещь может только её владелец.
     */

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

        return ItemMapper.toItemDto(itemService.update(item, itemId, userId));
    }

    /*
    Просмотр информации о конкретной вещи по её идентификатору. Эндпойнт GET /items/{itemId}.
    Информацию о вещи может просмотреть любой пользователь.
     */

    @GetMapping("{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto get(@PathVariable int itemId) {
        if (itemId <= 0) {
            throw new BadRequestException("Идентификатор должен быть положительным");
        }

        return ItemMapper.toItemDto(itemService.find(itemId));
    }

    /*
    Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой.
    Эндпойнт GET /items.
     */

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

    /*
    Поиск вещи потенциальным арендатором.
    Пользователь передаёт в строке запроса текст, и система ищет вещи,
    содержащие этот текст в названии или описании.
    Происходит по эндпойнту /items/search?text={text},
    в text передаётся текст для поиска.
    Проверьте, что поиск возвращает только доступные для аренды вещи.
     */
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
