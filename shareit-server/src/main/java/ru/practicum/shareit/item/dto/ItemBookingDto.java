package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemBookingDto {
    int id;
    String name;
    String description;
    Boolean available;
    Booking lastBooking;
    Booking nextBooking;
    List<CommentDto> comments;
    int owner;
    int requestId;

    @Data
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CommentDto {
        int id;
        String text;
        String authorName;
        LocalDateTime created;
    }
}
