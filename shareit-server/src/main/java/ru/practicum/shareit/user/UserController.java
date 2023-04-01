package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@FieldDefaults (level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserController {
    final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto add(@RequestBody UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        User returnUser = userService.add(user);

        return UserMapper.toDto(returnUser);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> get() {
        return userService.get()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@RequestBody UserDto userDto, @PathVariable int id) {
        User user = UserMapper.toUser(userDto);
        user.setId(id);
        User returnUser = userService.update(user);

        return UserMapper.toDto(returnUser);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto get(@PathVariable int id) {
        if (id <= 0) {
            throw new BadRequestException("Неверный запрос");
        }
        User user = userService.get(id);

        return UserMapper.toDto(user);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable int id) {
        if (id <= 0) {
            throw new BadRequestException("Неверный запрос");
        }
       userService.delete(id);
    }
}
