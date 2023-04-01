package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE)
public class ItemRequestMessageDto {
    int id;
    String description;
    int requesterId;
    LocalDateTime created;
}
