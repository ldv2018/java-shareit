package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BadRequestException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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
    final BookingMapper bookingMapper;
    final String USER = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto add(@RequestHeader(USER) int userId,
                                      @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        if (bookingRequestDto == null) {
            throw new BadRequestException("Пустой запрос");
        }
        bookingValidator.check(bookingRequestDto);
        Booking booking = bookingMapper.toBooking(bookingRequestDto);
        Booking returnBooking = bookingService.add(booking, userId);

        return bookingMapper.toBookingResponseDto(returnBooking);
    }

    @PatchMapping("{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponseDto update(@RequestHeader(USER) int userId,
                                      @PathVariable int bookingId,
                                      @RequestParam() boolean approved) {
        Booking booking = bookingService.updateStatus(userId, bookingId, approved);

        return bookingMapper.toBookingResponseDto(booking);
    }

    @GetMapping("{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponseDto get(@RequestHeader(USER) int userId,
                                  @PathVariable int bookingId) {
        Booking booking = bookingService.get(userId, bookingId);

        return bookingMapper.toBookingResponseDto(booking);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<BookingResponseDto> getAll(@RequestHeader(USER) int userId,
                                           @RequestParam(defaultValue = "ALL") String state) {
        List<Booking> bookings = bookingService.getAll(userId, state);
        List<BookingResponseDto> responseDtoList = new ArrayList<>();
        for (Booking b : bookings) {
            responseDtoList.add(bookingMapper.toBookingResponseDto(b));
        }

        return responseDtoList;
    }

    @GetMapping("owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingResponseDto> getAllByOwner(@RequestHeader(USER) int userId,
                                           @RequestParam(defaultValue = "ALL") String state) {
        List<Booking> bookings = bookingService.getAllByOwner(userId, state);
        List<BookingResponseDto> responseDtoList = new ArrayList<>();
        for (Booking b : bookings) {
            responseDtoList.add(bookingMapper.toBookingResponseDto(b));
        }

        return responseDtoList;
    }
}
