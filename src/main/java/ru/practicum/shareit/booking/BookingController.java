package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BadRequestException;

import javax.validation.Valid;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BookingController {
    final BookingService bookingService;
    final BookingValidator bookingValidator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto add(@RequestHeader("X-Sharer-User-Id") int userId,
                          @Valid @RequestBody BookingDto bookingDto) {
        if (bookingDto == null) {
            throw new BadRequestException("Пустой запрос");
        }
        bookingValidator.check(bookingDto);
        Booking booking = BookingMapper.toBooking(bookingDto);
        Booking returnBooking = bookingService.add(booking, userId);

        return BookingMapper.toDto(returnBooking);
    }
}
