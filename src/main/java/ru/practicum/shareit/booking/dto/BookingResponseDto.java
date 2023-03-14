package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.status.Status;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponseDto {
    int id;
    LocalDateTime start;
    LocalDateTime end;
    Item item;
    User booker;
    Status status;


    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class User {
        final int id;
        final String name;
    }

    @Data
    public static class Item {
        final int id;
        final String name;
        final String description;
    }
}
