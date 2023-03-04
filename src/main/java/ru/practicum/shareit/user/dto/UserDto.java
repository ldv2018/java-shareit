package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
@FieldDefaults (level = AccessLevel.PRIVATE)
public class UserDto {
    int id;
    @NotNull
    String name;
    @Email
    @NotNull
    String email;
}
