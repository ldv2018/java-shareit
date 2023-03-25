package ru.practicum.shareit.item.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class ItemServiceTest {

    @Mock
    ItemRepository mockItemRepository;
    @Mock
    UserRepository mockUserRepository;
    ItemService itemService;
    Item item1;
    Item updateItem;
    Item item2;
    User user;

    @BeforeEach
    void init() {
        itemService = new ItemService(mockItemRepository, mockUserRepository);
        user = new User(1, "name", "email@email.com");
        item1 = new Item(1, "item", "description", false, 1, 1);
        item2 = new Item(2, "name", "desc", true, 1, 2);
        updateItem = new Item(1, "updateItem", "updateDescription", true, 1, 1);
    }

    @Test
    public void saveWhenUserNotFoundTest() {
        Mockito.when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.add(item1, 2));
        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    public void saveTest() {
        Mockito.when(mockUserRepository.findById(Mockito.anyInt()))
                        .thenReturn(Optional.of(user));
        Mockito.when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item1);
        Item responseItem = itemService.add(item1, 1);
        Assertions.assertEquals(item1, responseItem);
    }

    @Test
    void updateWhenUserNotFoundTest() {
        Mockito.when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());
        NotFoundException exception1 = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.update(item1, 1, 1)
        );
        Assertions.assertEquals("Пользователь не найден", exception1.getMessage());
    }

    @Test
    void updateWhenItemNotFoundTest() {
        Mockito.when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito.when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.update(item1, 1, 1)
        );
        Assertions.assertEquals("Вещь не найдена", exception.getMessage());
    }

    @Test
    void updateWhenUserNotEqualsOwnerTest() {
        Mockito.when((mockUserRepository.findById(Mockito.anyInt())))
                .thenReturn(Optional.of(user));
        Mockito.when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(new Item(1,
                        "name",
                        "description",
                        true,
                        99,
                        1)));
        Exception exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.update(item1, 1, 1)
        );
        Assertions.assertEquals("Владелец не совпадает с владельцем из запроса", exception.getMessage());
    }

    @Test
    void updateTest() {
        Mockito.when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito.when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(new Item(1,
                        "name",
                        "description",
                        false,
                        1,
                        1)));
        Mockito.when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenReturn(updateItem);
        Item updateItem1 = itemService.update(updateItem, 1, 1);
        Assertions.assertEquals(updateItem1.getAvailable(), true);
        Assertions.assertEquals(updateItem1.getName(), "updateItem");
        Assertions.assertEquals(updateItem1.getDescription(), "updateDescription");
    }

    @Test
    void findTest() {
        Mockito.when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.find(2));
        Assertions.assertEquals("Вещь не найдена", exception.getMessage());

        Mockito.when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item1));
        Item findItem = itemService.find(1);
        Assertions.assertEquals(item1, findItem);
    }

    @Test
    void findAllByUserTest() {
        List<Item> items = List.of(item1, item2);
        Page<Item> page = new PageImpl<>(items);
        Mockito.when(mockItemRepository.getAllByOwnerOrderByIdAsc(Mockito.anyInt(), Mockito.any(Pageable.class)))
                .thenReturn(page);
        List<Item> responseItems = itemService.findAllByUser(1, 0, 2);
        Assertions.assertAll(
                () -> Assertions.assertNotNull(responseItems),
                () -> Assertions.assertEquals(items.get(0), item1),
                () -> Assertions.assertEquals(items.get(1), item2)
        );
    }

    @Test
    void findByReviewIfStringIsBlankTest() {
        List<Item> items = itemService.findByReview("");
        Assertions.assertTrue(items.isEmpty());
    }
}
