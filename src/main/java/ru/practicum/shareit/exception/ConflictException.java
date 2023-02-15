package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends RuntimeException{

    public ConflictException(HttpStatus httpStatus, String msg) {
        super(msg);
    }
}
