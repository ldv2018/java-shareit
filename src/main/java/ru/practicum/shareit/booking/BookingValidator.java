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
    public void check(@Valid BookingRequestDto bookingDto) {
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new BadRequestException("Время конца бронирования раньше времени начала");
        }
    }
}
