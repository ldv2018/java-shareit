package ru.practicum.shareit.itemRequest.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestServiceTest {
    ItemRequestService itemRequestService;
    @Mock
    ItemRequestRepository mockItemRequestRepository;
    @Mock
    UserRepository mockUserRepository;
    @Mock
    ItemRepository mockItemRepository;
    User user;
    ItemRequest itemRequest;

    @BeforeEach
    void init() {
        itemRequestService = new ItemRequestService(
                mockItemRequestRepository,
                mockUserRepository,
                mockItemRepository
        );
        user = new User(1, "user", "email@email.com");
        itemRequest = new ItemRequest(1,
                "description",
                99,
                null);
    }

    @Test
    public void throwIfUserNotFoundTest() {
        Mockito.when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemRequestService.add(itemRequest, 1)
        );
        Assertions.assertEquals("Пользователь 1 не найден", exception.getMessage());
    }

    @Test
    public void addTest() {
        Mockito.when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        Mockito.when(mockItemRequestRepository.save(Mockito.any(ItemRequest.class)))
                .thenReturn(itemRequest);
        ItemRequest savedItemRequest = itemRequestService.add(itemRequest, 1);
        Assertions.assertEquals(savedItemRequest.getRequesterId(), 1);
        Assertions.assertNotNull(savedItemRequest.getCreated());
    }

    @Test
    public void getTest() {
        Mockito.when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        ItemRequest ir1 = new ItemRequest(1,
                "desc",
                1,
                LocalDateTime.now().minusDays(1));
        ItemRequest ir2 = new ItemRequest(2,
                "description",
                1,
                LocalDateTime.now().minusHours(5));
        List<ItemRequest> itemRequests = List.of(ir1, ir2);
        Mockito.when(mockItemRequestRepository.findAllByRequesterIdOrderByCreated(1))
                .thenReturn(itemRequests);
        Item item1 = new Item();
        Item item2 = new Item();
        List<Item> items = List.of(item1, item2);
        Mockito.when(mockItemRepository.findAllByRequestId(Mockito.anyInt()))
                .thenReturn(items);
        Map<ItemRequest, List<Item>> itemRequestAndItemAnswer =
                itemRequestService.get(1);
        Assertions.assertEquals(itemRequestAndItemAnswer.size(), 2);
    }
}
