package ru.practicum.shareit.user.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.implement.Storage;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Slf4j
@Repository
@FieldDefaults (level = AccessLevel.PRIVATE)
public class InMemoryUserStorage implements Storage<User> {
    Map<Integer, User> users = new HashMap<>();
    int id = 0;

    @Override
    public User add(User user) {
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
        users.replace(user.getId(), user);
        log.info("пользователь с id {} обновлен", user.getId());

        return user;
    }

    @Override
    public Optional<User> find(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> find(String email) {
        for (User u : users.values()) {
            if (u.getEmail().equals(email)) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> findAll(String str) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(int id) {
        users.remove(id);
        log.info("пользователь {} удален", id);
    }

    private int generateId() {
        return ++id;
    }
}
