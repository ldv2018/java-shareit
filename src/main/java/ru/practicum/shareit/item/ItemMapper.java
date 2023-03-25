package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest()
        );
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getOwner(),
                itemDto.getRequest()
        );
    }

    public static ItemBookingDto toItemBookingDto(Item item) {
        return new ItemBookingDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                null,
                null,
                null,
                item.getOwner(),
                item.getRequest()
        );
    }

    public static ItemBookingDto.CommentDto toCommentDto(Comment comment, User author) {
        return new ItemBookingDto.CommentDto(
                comment.getId(),
                comment.getText(),
                author.getName(),
                comment.getCreated()
        );
    }
}