package ru.practicum.shareit.implement;

import java.util.List;
import java.util.Optional;

public interface Storage<T> {
    T add(T t);

    List<T> findAll();

    List<Integer> findAllId();

    T update(T t);

    Optional<T> find(int id);

    void delete(int id);
}
