package ru.practicum.shareit.item.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@FieldDefaults (level = AccessLevel.PRIVATE)
public class InMemoryItemStorage implements ItemStorage{

    Map<Integer, Item> items = new HashMap<>();
    int id = 0;

    @Override
    public Item add(Item item) {
        item.setId(generateId());
        items.put(item.getId(), item);
        log.info("вещь добавлена в хранилище");
        return item;
    }

    @Override
    public Item update(Item item, int itemId) {
        isItemExist(itemId);
        Item storageItem = items.get(itemId);
        if (item.getAvailable() != null) {
            storageItem.setAvailable(item.getAvailable());
            log.info("Обновлен статус вещи {}", itemId);
        };
        if (item.getName() != null) {
            storageItem.setName(item.getName());
            log.info("Обновлено имя вещи {}", itemId);
        };
        if (item.getDescription() != null) {
            storageItem.setDescription(item.getDescription());
            log.info("Обновлено описание вещи {}", itemId);
        };
        items.replace(itemId, storageItem);
        log.info("вещь обновлена в хранилище");
        return storageItem;
    }

    @Override
    public Item find(int id) {
        isItemExist(id);
        log.info("вещь найдена в хранилище");
        return items.get(id);
    }

    @Override
    public List<Item> findAllByOwner(int id) {
        isItemsNotEmpty();
        List<Item> itemsByOwner = new ArrayList<>();
        try {
            items.forEach((key, value) -> {
                if (value.getOwner() == id) {
                    itemsByOwner.add(value);
                    log.info("вещь найдена по пользователю");
                }
            });
        } catch (NullPointerException e) {
            throw new ConflictException(HttpStatus.CONFLICT, "Вещей нет");
        }
        return itemsByOwner;
    }

    @Override
    public Item findByReview(String review) {
        isItemsNotEmpty();
        for (Map.Entry<Integer, Item> entry : items.entrySet()) {
            String str = entry.getValue().getDescription() + " "
                    + entry.getValue().getName();
            if (str.contains(review)) {
                log.info("вещь найдена по описанию");
                return entry.getValue();
            }
        }
        return null;
    }

    private int generateId() {
        return ++id;
    }

    private void isItemExist(int id) {
        if (!items.containsKey(id)) {
            throw new BadRequestException("Такой вещи не существует");
        }
    }

    private void isItemsNotEmpty() {
        if (items.isEmpty()) {
            throw new ConflictException(HttpStatus.CONFLICT, "Список вещей пуст");
        }
    }
}
