package ru.practicum.shareit.user.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Slf4j
@Repository
@FieldDefaults (level = AccessLevel.PRIVATE)
public class InMemoryUserStorage implements UserStorage {
    Map<Integer, User> users = new HashMap<>();
    List<String> emails = new ArrayList<>();
    int id = 0;

    @Override
    public User add(User user) {
        if (isEmailExist(user)) {
            throw new ConflictException(HttpStatus.CONFLICT, "такой email уже существует");
        }
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("пользователь с id {} добавлен", user.getId());

        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User update(User user) {
        isUserExist(user.getId());

        User updateUser = users.get(user.getId());
        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (isEmailExist(user) && !Objects.equals(user.getEmail(), users.get(user.getId()).getEmail())) {
                throw new ConflictException(HttpStatus.CONFLICT, "такой email уже существует");
            }
            emailsUpdate(user.getId());
            updateUser.setEmail(user.getEmail());
        }
        users.replace(user.getId(), updateUser);
        log.info("пользователь с id {} обновлен", user.getId());
        return updateUser;
    }

    @Override
    public User find(int id) {
        isUserExist(id);
        return users.get(id);
    }

    @Override
    public void delete(int id) {
        isUserExist(id);
        emails.remove(users.get(id).getEmail());
        users.remove(id);
        log.info("пользователь {} удален", id);
    }

    @Override
    public void throwIfUserNotFound(int id) {
        if (!users.containsKey(id)) {
            log.info("Пользователь не найден");
            throw new NotFoundException("Пользователь не найден");
        }
    }

    private int generateId() {
        return ++id;
    }

    private void isUserExist(int id) {
        if (!users.containsKey(id)) {
            log.info("нет такого пользователя");
            throw new BadRequestException("Пользователь не существует");
        }
    }

    private void emailsUpdate(int id) {
        emails.remove(users.get(id).getEmail());
    }

    private boolean isEmailExist(User user) {
        if (emails.contains(user.getEmail())) {
            return true;
        } else {
            emails.add(user.getEmail());
            return false;
        }
    }
}
