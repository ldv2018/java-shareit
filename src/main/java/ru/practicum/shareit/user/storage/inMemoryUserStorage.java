package ru.practicum.shareit.user.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@FieldDefaults (level = AccessLevel.PRIVATE)
public class inMemoryUserStorage implements UserStorage{
    Map<Integer, User> users = new HashMap<>();
    List<String> emails = new ArrayList<>();
    int id = 0;

    @Override
    public User add(User user) {
        if (isEmailExist(user)) {
            throw new RuntimeException();
        }
        user.setId(getId());
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
        if (!users.containsKey(user.getId())) {
            log.info("нет такого пользователя");
            throw new RuntimeException();
        }
        users.replace(user.getId(), user);
        log.info("пользователь с id {} обновлен", user.getId());
        return user;
    }

    @Override
    public User find(int id) {
        if (!users.containsKey(id)) {
            log.info("нет такого пользователя");
            throw new RuntimeException();
        }
        return users.get(id);
    }

    private int getId() {
        return id + 1;
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
