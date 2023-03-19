package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults (level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Slf4j
public class ItemService {

    final ItemRepository itemStorage;
    final UserRepository userStorage;

    public Item add(Item item, int id) {
        User user = userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return itemStorage.save(item);
    }

    public Item update(Item item, int itemId, int userId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item updateItem = itemStorage.findById(itemId)
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

        return itemStorage.save(updateItem);
    }

    public Item find(int id) {
        return itemStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
    }

    public List<Item> findAllByUser(int id, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        Page<Item> items = itemStorage.getAllByOwnerOrderByIdAsc(id, pageable);
        return items.getContent();
    }

    public List<Item> findByReview(String str) {
        if (str.isBlank()) {
            log.info("Пустая строка для поиска");
            return new ArrayList<>();
        }

        return itemStorage.search(str);
    }
}
