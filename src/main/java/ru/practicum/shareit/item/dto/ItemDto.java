package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@AllArgsConstructor
@Data
@FieldDefaults (level = AccessLevel.PRIVATE)
public class ItemDto {
    int id;
    @NotNull
    @NotBlank
    String name;
    @NotNull
    @NotBlank
    String description;
    @NotNull
    Boolean available;
    int owner;
    int requestId;
}
