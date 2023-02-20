package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.implement.Storage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults (level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Slf4j
public class ItemService {

    final Storage<Item> itemStorage;
    final Storage<User> userStorage;

    public Item add(Item item, int id) {
        User user = userStorage.find(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return itemStorage.add(item);
    }

    public Item update(Item item, int itemId, int userId) {
        User user = userStorage.find(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item updateItem = itemStorage.find(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        if (updateItem.getOwner() != userId) {
            throw new NotFoundException("Владелец не совпадает с " +
                    "владельцем из запроса");
        }
        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
            log.info("Обновлен статус вещи {}", itemId);
        }
        if (item.getName() != null) {
            updateItem.setName(item.getName());
            log.info("Обновлено имя вещи {}", itemId);
        }
        if (item.getDescription() != null) {
            updateItem.setDescription(item.getDescription());
            log.info("Обновлено описание вещи {}", itemId);
        }

        return itemStorage.update(updateItem);
    }

    public Item find(int id) {
        return itemStorage.find(id)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
    }

    public List<Item> findAllByUser(int id) {
        List<Item> items = itemStorage.findAll();
        List<Item> itemsByOwner = new ArrayList<>();
        try {
            for (Item item : items) {
                if (item.getOwner() == id) {
                    itemsByOwner.add(item);
                    log.info("вещь найдена по пользователю");
                }
            }
        } catch (NullPointerException e) {
            throw new ConflictException(HttpStatus.CONFLICT, "Вещей нет");
        }

        return itemsByOwner;
    }

    public List<Item> findByReview(String str) {
        List<Item> items = itemStorage.findAll();
        List<Item> findedItems = new ArrayList<>();
        if (str.isBlank()) {
            log.info("пустая строка для поиска");
            return findedItems;
        }
        for (Item item : items) {
            String searchStr = item.getDescription() + " "
                    + item.getName();
            if (searchStr.toLowerCase().contains(str.toLowerCase()) && item.getAvailable()) {
                log.info("вещь найдена по описанию");
                findedItems.add(item);
            }
        }

        return findedItems;
    }
}
