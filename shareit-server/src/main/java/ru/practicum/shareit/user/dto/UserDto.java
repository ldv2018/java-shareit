package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Data
@FieldDefaults (level = AccessLevel.PRIVATE)
public class UserDto {
    int id;
    String name;
    String email;
}
