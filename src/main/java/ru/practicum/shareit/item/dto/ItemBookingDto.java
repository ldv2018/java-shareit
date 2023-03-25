package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Booking;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemBookingDto {
    int id;
    @NotNull
    @NotBlank
    String name;
    @NotNull
    @NotBlank
    String description;
    @NotNull
    Boolean available;
    Booking lastBooking;
    Booking nextBooking;
    List<CommentDto> comments;
    int owner;
    int request;

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
