package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemStorage {

    Item add(Item item);

    Item update(Item item, int id);

    Item find(int id);

    List<Item> findAllByOwner(int id);

    Item findByReview(String review);
}
