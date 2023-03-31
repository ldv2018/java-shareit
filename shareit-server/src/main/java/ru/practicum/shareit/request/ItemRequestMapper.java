package ru.practicum.shareit.request;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestMessageDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;

public class ItemRequestMapper {
    public static ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest) {
        return new ItemRequestResponseDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequesterId(),
                itemRequest.getCreated(),
                new ArrayList<ItemRequestResponseDto.Answer>()
        );
    }

    public static ItemRequest toItemRequest(ItemRequestMessageDto itemRequestMessageDto) {
        return new ItemRequest(
                itemRequestMessageDto.getId(),
                itemRequestMessageDto.getDescription(),
                itemRequestMessageDto.getRequesterId(),
                itemRequestMessageDto.getCreated()
        );
    }

    public static ItemRequestResponseDto.Answer toAnswer(Item item) {
        return new ItemRequestResponseDto.Answer(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequestId(),
                item.getOwner()
        );
    }
}
