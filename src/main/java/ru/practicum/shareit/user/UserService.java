package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@AllArgsConstructor
@Slf4j
//@Transactional(readOnly = true)
public class UserService {
    final UserRepository userStorage;

    public User add(User user) {
        return userStorage.save(user);
    }

    public List<User> get() {
        return userStorage.findAll();
    }

    public User update(User user) {
        User updateUser = userStorage.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            updateUser.setEmail(user.getEmail());
        }

        return userStorage.save(updateUser);
    }

    public User get(int id) {
        return userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public void delete(int id) {
        User user = userStorage.findById(id)
                .orElseThrow(() -> new BadRequestException("Такого пользователя не существует"));

        userStorage.delete(user);
    }
}
