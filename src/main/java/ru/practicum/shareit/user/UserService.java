package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.implement.Storage;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    final Storage<User> userStorage;

    public User add(User user) {
        throwIfEmailInUse(user);

        return userStorage.add(user);
    }

    public List<User> get() {
        return userStorage.findAll();
    }

    public User update(User user) {
        User updateUser = userStorage.find(user.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (!user.getEmail().equals(updateUser.getEmail())) {
                throwIfEmailInUse(user);
            }
            updateUser.setEmail(user.getEmail());
        }

        return userStorage.update(updateUser);
    }

    public User get(int id) {
        return userStorage.find(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public void delete(int id) {
        User user = userStorage.find(id)
                .orElseThrow(() -> new BadRequestException("Такого пользователя не существует"));

        userStorage.delete(id);
    }

    private void throwIfEmailInUse(User user) {
        List<User> users = userStorage.findAll();
        for (User u : users) {
            if (Objects.equals(u.getEmail(), user.getEmail())) {
                throw new ConflictException(HttpStatus.CONFLICT, "Такой email уже используется");
            }
        }
    }
}
