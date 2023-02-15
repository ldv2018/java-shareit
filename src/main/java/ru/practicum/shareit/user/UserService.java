package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@FieldDefaults (level = AccessLevel.PRIVATE)
@Service
public class UserService {
    final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        return userStorage.add(user);
    }

    public List<User> get() {
        return userStorage.findAll();
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User get(int id) {
        return userStorage.find(id);
    }

    public void delete(int id) {
        userStorage.delete(id);
    }
}
