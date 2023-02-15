package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User add(User user);

    List<User> findAll();

    User update(User user);

    User find(int id);

    void delete(int id);
}
