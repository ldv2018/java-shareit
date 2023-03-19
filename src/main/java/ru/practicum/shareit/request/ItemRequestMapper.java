package ru.practicum.shareit.request;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.ArrayList;

@Service
public class ItemRequestMapper {
    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequesterId(),
                itemRequest.getCreated(),
                new ArrayList<ItemRequestDto.Answer>()
        );
    }

    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return new ItemRequest(
                itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                itemRequestDto.getRequesterId(),
                itemRequestDto.getCreated()
        );
    }

    public ItemRequestDto.Answer toAnswer (Item item) {
        return new ItemRequestDto.Answer(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequestId(),
                item.getOwner()
        );
    }
}
