package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.validator.Validator;

import javax.validation.Valid;

@Validated
@Component
public class BookingValidator implements Validator<BookingRequestDto> {

    @Override
    public void throwIfNotValid(@Valid BookingRequestDto bookingDto) {
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
