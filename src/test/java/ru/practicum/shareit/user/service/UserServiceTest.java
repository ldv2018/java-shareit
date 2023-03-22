package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class UserServiceTest {

    UserService userService;
    @Mock
    UserRepository mockUserRepository;
    private User user;

    @BeforeEach
    void init() {
        userService = new UserService(mockUserRepository);
        user = new User();
        user.setName("name");
        user.setEmail("email@email.com");
        user.setId(1);
    }

    @Test
    void saveTest() {
        Mockito.when(mockUserRepository.save(Mockito.any(User.class)))
                .thenReturn(user);
        User user2 = userService.add(user);
        Assertions.assertEquals(user, user2);
    }

    @Test
    void patchTest() {
        User user2 = new User();
        user2.setId(1);
        user2.setName("name2");
        user2.setEmail("email2@email.com");
        Mockito.when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito.when(mockUserRepository.save(Mockito.any(User.class)))
                .thenReturn(user2);
        User user3 = userService.update(user);
        Assertions.assertEquals(user3, user2);
        Mockito.when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(RuntimeException.class,
                () -> userService.update(user));
        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    void getTest() {
        Mockito.when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        User user2 = userService.get(user.getId());
        Assertions.assertEquals(user, user2);
        Mockito.when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(RuntimeException.class,
                () -> userService.get(1));
        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    void getAllTest() {
        List<User> users = new ArrayList<>();
        User user2 = new User();
        user2.setId(2);
        user2.setName("Name2");
        user2.setEmail("email2@email.com");
        users.add(user);
        users.add(user2);
        Mockito.when(mockUserRepository.findAll())
                .thenReturn(users);
        List<User> users2 = userService.get();
        Assertions.assertAll(
                () -> Assertions.assertNotNull(users2),
                () -> Assertions.assertEquals(users2.get(1), user2),
                () -> Assertions.assertEquals(users2.get(0), user)
        );
    }

}