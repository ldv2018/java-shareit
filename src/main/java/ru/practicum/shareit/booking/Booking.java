package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.status.Status;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@AllArgsConstructor
@Data
@FieldDefaults (level = AccessLevel.PRIVATE)
public class Booking {
    int id;
    LocalDate start;
    LocalDate end;
    Item item;
    Status status;
    User user;
    String review;
}
