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
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new BadRequestException("Время конца раньше времени начала");
        }
        if (bookingDto.getEnd().equals(bookingDto.getStart())) {
            throw new BadRequestException("Время конца не должно быть равно времени начала");
        }
    }
}