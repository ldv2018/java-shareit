package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@FieldDefaults (level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ItemService {

    final ItemStorage itemStorage;
    final UserStorage userStorage;

    public Item add(Item item, int id) {
        userStorage.throwIfUserNotFound(id);
        return itemStorage.add(item);
    }

    public Item update(Item item, int itemId, int userId) {
        userStorage.throwIfUserNotFound(userId);
        if (itemStorage.find(itemId).getOwner() != userId) {
            throw new NotFoundException("Владелец не совпадает с " +
                    "владельцем из запроса");
        }

        return itemStorage.update(item, itemId);
    }

    public Item find(int id) {
        return itemStorage.find(id);
    }

    public List<Item> findAllByUser(int id) {
        return itemStorage.findAllByOwner(id);
    }

    public Item findByReview(String str) {
        return itemStorage.findByReview(str);
    }
}
