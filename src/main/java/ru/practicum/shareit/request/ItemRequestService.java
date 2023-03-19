package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@AllArgsConstructor
@Slf4j
public class ItemRequestService {
    final ItemRequestRepository itemRequestRepository;
    final UserRepository userRepository;
    final ItemRepository itemRepository;

    public ItemRequest add(ItemRequest itemRequest, int userId) {
        log.info("Добавление itemRequest от пользователя " + userId);
        throwIfUserNotFound(userId);
        itemRequest.setRequesterId(userId);
        itemRequest.setCreated(LocalDateTime.now());
        log.info("itemRequest пользователя " + userId + "добавлен");

        return itemRequestRepository.save(itemRequest);
    }

    public Map<ItemRequest, List<Item>> get(int userId) {
        log.info("Получение List<ItemRequest> от пользователя " + userId);
        throwIfUserNotFound(userId);
        List<ItemRequest> UserItemRequests = itemRequestRepository.findAllByRequesterIdOrderByCreated(userId);
        log.info("List<ItemRequest> от пользователя " + userId + " получен");
        Map<ItemRequest, List<Item>> itemRequestAndItemAnswer = new HashMap<>();
        for (ItemRequest itemRequest : UserItemRequests) {
            List<Item> itemAnswers = itemRepository.findAllByRequestId(itemRequest.getId());
            itemRequestAndItemAnswer.put(itemRequest, itemAnswers);
        }

        return itemRequestAndItemAnswer;
    }

    public Map<ItemRequest, List<Item>> getAll(int userId, int from, int size) {
        log.info("Получение List<ItemRequest>. Параметры: userId = " + userId + " from = " + from + "; size = " + size);
        Pageable pageable = PageRequest.of(from, size, Sort.by("created"));
        throwIfUserNotFound(userId);
        Page<ItemRequest> itemRequestPage = itemRequestRepository.findAll(userId, pageable);
        Map<ItemRequest, List<Item>> itemRequestsAndItems = new HashMap<>();
        for (ItemRequest itemRequest : itemRequestPage.getContent()) {
            List<Item> itemAnswers = itemRepository.findAllByRequestId(itemRequest.getId());
            itemRequestsAndItems.put(itemRequest, itemAnswers);
        }
        log.info("Получена страница с itemRequests");

        return itemRequestsAndItems;
    }

    public Map<ItemRequest, List<Item>> get(int userId, int requestId) {
        log.info("Получение ItemRequest. Параметры: userId = " + userId + ", requestId = " + requestId);
        throwIfUserNotFound(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("ItemRequest c id " + requestId + " не найден"));
        Map<ItemRequest, List<Item>> itemRequestsAndItems = new HashMap<>();
        itemRequestsAndItems.put(itemRequest, itemRepository.findAllByRequestId(itemRequest.getId()));

        return itemRequestsAndItems;
    }

    private void throwIfUserNotFound(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь " + userId + " не найден"));
    }
}
