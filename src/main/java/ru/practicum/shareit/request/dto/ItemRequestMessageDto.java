package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE)
public class ItemRequestMessageDto {
    int id;
    @NotBlank
    @NotNull
    String description;
    @NotNull
    int requesterId;
    LocalDateTime created;
}
