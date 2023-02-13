package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */
@AllArgsConstructor
@Data
@FieldDefaults (level = AccessLevel.PRIVATE)
public class Item {
    int id;
    String name;
    String description;
    Boolean isAvailable;
    User owner;
    ItemRequest request;
}
