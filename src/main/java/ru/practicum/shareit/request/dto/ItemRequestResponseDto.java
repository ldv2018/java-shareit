package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestResponseDto {
    int id;
    String description;
    int requesterId;
    LocalDateTime created;
    List<Answer> items;

    @AllArgsConstructor
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Answer {
        int id;
        String name;
        String description;
        boolean available;
        int requestId;
        int ownerId;
    }
}
