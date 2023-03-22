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
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class ItemServiceTest {

    @Mock
    ItemRepository mockItemRepository;
    @Mock
    UserRepository mockUserRepository;
    ItemService itemService;
    ItemDto itemResponseDto1;
    ItemDto itemMessageDto1;
    ItemDto itemResponseDto2;
    ItemDto itemMessageDto2;
    Item item1;
    Item item2;
    User user;
    ItemDto itemRequest1;

    @BeforeEach
    void init() {
        itemService = new ItemService(mockItemRepository, mockUserRepository);
        user = new User(1, "name", "email@email.com");
        item1 = new Item(1, "item", "description", false, 1, 1);
        item2 = new Item(2, "name", "desc", true, 1, 2);
        Item updateItem = new Item(1, "updateItem", "updateDescription", true, 1, 1);
    }

    @Test
    public void saveTest() {
        Mockito.when(itemService.add(item1, Mockito.anyInt()))
                .thenThrow(new NotFoundException("Пользователь не найден"));
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.add(item1, 2));
        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
        Mockito.when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item1);
        Item responseItem = itemService.add(item1, 1);
        Assertions.assertEquals(item1, responseItem);
    }

    @Test
    void updateTest() {
        Mockito.when(itemService.update(Mockito.any(Item.class), Mockito.anyInt(), Mockito.anyInt()))
                .thenThrow(new NotFoundException("Вещь не найдена"));
        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.update(item1, 3, 1)
        );
        Assertions.assertEquals("Вещь не найдена", exception.getMessage());
        Mockito.when(itemService.update(Mockito.any(Item.class), Mockito.anyInt(), Mockito.anyInt()))
                .thenThrow(new NotFoundException("Владелец не совпадает с " +
                        "владельцем из запроса"));
        exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.update(item1, 1, 1)
        );
        Assertions.assertEquals("Владелец не совпадает с владельцем из запроса", exception.getMessage());
        Mockito.when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenReturn(updateItem);
        Item updateItem1 = userService.update(updateItem, 1, 1);
        Assertions.assertEquals(updateItem1.getAvailable(), true);
        Assertions.assertEquals(updateItem1.getName(), "updateItem");
        Assertions.assertEquals(updateItem1.getDescription(), "updateDescription");
    }

    @Test
    void findTest() {
        Mockito.when(mockItemRepository.findById(Mockito.anyInt()))
                .thenThrow(new NotFoundException("Вещь не найдена"));
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.find(2));
        Assertions.assertEquals("Вещь не найдена", exception.getMessage());

        Mockito.when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(item);
        Item findItem = itemService.find(1);
        Assertions.assertEquals(item, findItem);
    }

    @Test
    void findAllByUserTest() {
        List<Item> items = List.of(item, item1);
        Mockito.when(mockItemRepository.getAllByOwnerOrderByIdAsc(Mockito.anyInt(), Mockito.any(Pageable.class)))
                .thenReturn(items);
        List<Item> responseItems = itemService.findAllByUser(1, 0, 2);
        Assertions.assertAll(
                () -> Assertions.assertNotNull(responseItems),
                () -> Assertions.assertEquals(items.get(0), item),
                () -> Assertions.assertEquals(items.get(1), item1)
        );
    }

    @Test
    void findByReviewTest() {

    }
}
