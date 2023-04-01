package ru.practicum.shareit.booking.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.dto.BookingMessageDto;
import ru.practicum.shareit.exception.BadRequestException;

import javax.validation.Valid;

@Validated
@Component
public class BookingMessageDtoValidator implements Validator<BookingMessageDto> {
    public void throwIfNotValid(@Valid BookingMessageDto bookingDto) {
        if (bookingDto == null) {
            throw new BadRequestException("Пустой запрос");
        }
        if (bookingDto.getEnd() == null) {
            throw new BadRequestException("Не указано время конца бронирования");
        }
        if (bookingDto.getStart() == null) {
            throw new BadRequestException("Не указано время начала бронирования");
        }
        if (bookingDto.getStart().compareTo(bookingDto.getEnd()) == 0) {
            throw new BadRequestException("Время начала и окончания бронирования совпадают");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new BadRequestException("Время конца бронирования раньше времени начала");
        }
    }
}