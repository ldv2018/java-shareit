package ru.practicum.shareit.validator;

import org.springframework.validation.annotation.Validated;

@Validated
public interface Validator<T> {
    void throwIfNotValid(T t);
}
