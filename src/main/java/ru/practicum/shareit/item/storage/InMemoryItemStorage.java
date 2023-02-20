package ru.practicum.shareit.item.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.implement.Storage;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Slf4j
@Repository
@FieldDefaults (level = AccessLevel.PRIVATE)
public class InMemoryItemStorage implements Storage<Item> {

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
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public List<Integer> findAllId() {
        return new ArrayList<>(items.keySet());
    }

    @Override
    public Item update(Item item) {
        items.replace(item.getId(), item);
        log.info("вещь обновлена в хранилище");

        return item;
    }

    @Override
    public Optional<Item> find(int id) {
        log.info("вещь найдена в хранилище");

        return Optional.ofNullable(items.get(id));
    }

    @Override
    public void delete(int id) {
        items.remove(id);
    }

    private int generateId() {
        return ++id;
    }
}
