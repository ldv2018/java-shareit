package ru.practicum.shareit.booking.validator;

import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface Validator<T> {
    void throwIfNotValid(@Valid T t);
}