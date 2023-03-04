package ru.practicum.shareit.implement;

import java.util.List;
import java.util.Optional;

public interface Storage<T> {
    T add(T t);

    List<T> findAll();

    T update(T t);

    Optional<T> find(int id);

    Optional<T> find(String str);

    List<T> findAll(int id);

    List<T> findAll(String str);

    void delete(int id);
}
