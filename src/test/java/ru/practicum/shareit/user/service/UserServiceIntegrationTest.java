package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceIntegrationTest {

    final UserService userService;
    User user1;
    User user2;

    @BeforeEach
    void init() {
        user1 = new User(1, "Name1", "email1@email.com");
        user2 = new User(2, "Name2", "email2@email.com");
    }

    @Test
    void saveTest() {
        User userTest = userService.add(user1);
        Assertions.assertEquals(user1, userTest);
    }

    @Test
    void patchTest() {
        userService.add(user1);
        user2.setId(1);
        User userTest = userService.update(user2);
        Assertions.assertEquals(user2, userTest);
        user2.setId(5);
        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.update(user2);
        });
    }

    @Test
    void getTest() {
        userService.add(user1);
        User userTest = userService.get(1);
        Assertions.assertAll(
                () -> Assertions.assertEquals(user1, userTest),
                () -> Assertions.assertEquals(userTest.getId(), userTest.getId()),
                () -> Assertions.assertThrows(RuntimeException.class, () -> userService.get(5))
        );
    }

    @Test
    void getTestWrong() {
        Assertions.assertThrows(RuntimeException.class, () -> userService.get(5));
    }

    @Test
    void getAllTest() {
        userService.add(user1);
        userService.add(user2);
        List<User> users = List.of(user1, user2);
        List<User> usersTest = userService.get();
        Assertions.assertEquals(usersTest, users);
    }

    @Test
    void deleteTest() {
        userService.add(user1);
        userService.add(user2);
        List<User> users = List.of(user1, user2);
        List<User> usersTest = userService.get();
        Assertions.assertEquals(usersTest, users);
        userService.delete(1);
        usersTest = userService.get();
        Assertions.assertNotEquals(usersTest, users);
    }
}