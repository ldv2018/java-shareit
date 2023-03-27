package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

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
    final BookingService bookingService;
    final CommentService commentService;
    final UserService userService;

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
    public ItemBookingDto get(@RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable int itemId) {
        if (itemId <= 0) {
            throw new BadRequestException("Идентификатор должен быть положительным");
        }
        Item item = itemService.find(itemId);
        ItemBookingDto itemBookingDto = ItemMapper.toItemBookingDto(item);
        if (item.getOwner() == userId) {
            Booking next = bookingService.getNextBookingByItemId(itemBookingDto.getId());
            Booking last = bookingService.getLastBookingByItemId(itemBookingDto.getId());

            itemBookingDto.setLastBooking(last);
            itemBookingDto.setNextBooking(next);
        }
        List<ItemBookingDto.CommentDto> commentsDto = new ArrayList<>();
        for (Comment c : commentService.findAllByItemId(itemId)) {
            commentsDto.add(ItemMapper.toCommentDto(c, userService.get(c.getAuthorId())));
        }
        itemBookingDto.setComments(commentsDto);

        return itemBookingDto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemBookingDto> findAllByOwner(@RequestHeader("X-Sharer-User-Id") int userId,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "99") int size) {
        if (userId <= 0) {
            throw new BadRequestException("Идентификатор должен быть положительным");
        }
        if (from < 0 || size < 1) {
            log.info("Получены неверные значения size = " + size + ", from = " + from);
            throw new BadRequestException("Параметры пагинации должны быть >= 0");
        }
        List<ItemBookingDto> dto = new ArrayList<>();
        for (Item item : itemService.findAllByUser(userId, from, size)) {
            ItemBookingDto itemBookingDto = ItemMapper.toItemBookingDto(item);
            Booking next = bookingService.getNextBookingByItemId(itemBookingDto.getId());
            Booking last = bookingService.getLastBookingByItemId(itemBookingDto.getId());

            itemBookingDto.setLastBooking(last);
            itemBookingDto.setNextBooking(next);

            dto.add(itemBookingDto);
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

    @PostMapping("{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public ItemBookingDto.CommentDto addComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                                @PathVariable int itemId,
                                                @RequestBody @Valid Comment comment) {
        Comment c = commentService.add(userId, itemId, comment);

        return ItemMapper.toCommentDto(c, userService.get(c.getAuthorId()));
    }
}
