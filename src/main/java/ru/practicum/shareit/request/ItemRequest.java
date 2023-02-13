package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@AllArgsConstructor
@Data
@FieldDefaults (level = AccessLevel.PRIVATE)
public class ItemRequest {
    int id;
    String description;
    User requester;
    LocalDateTime created;
}
