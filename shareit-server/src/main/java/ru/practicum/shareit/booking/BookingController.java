package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BadRequestException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    final BookingService bookingService;
    final BookingMapper bookingMapper;
    final String user = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto add(@RequestHeader(user) int userId,
                                      @RequestBody BookingRequestDto bookingRequestDto) {
        Booking booking = bookingMapper.toBooking(bookingRequestDto);
        Booking returnBooking = bookingService.add(booking, userId);

        return bookingMapper.toBookingResponseDto(returnBooking);
    }

    @PatchMapping("{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponseDto update(@RequestHeader(user) int userId,
                                      @PathVariable int bookingId,
                                      @RequestParam() boolean approved) {
        Booking booking = bookingService.updateStatus(userId, bookingId, approved);

        return bookingMapper.toBookingResponseDto(booking);
    }

    @GetMapping("{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponseDto get(@RequestHeader(user) int userId,
                                  @PathVariable int bookingId) {
        Booking booking = bookingService.get(userId, bookingId);

        return bookingMapper.toBookingResponseDto(booking);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<BookingResponseDto> getAll(@RequestHeader(user) int userId,
                                           @RequestParam(defaultValue = "ALL") String state,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "99") int size) {
        if (from < 0 || size < 1) {
            log.info("Получены неверные значения size = " + size + ", from = " + from);
            throw new BadRequestException("Параметры пагинации должны быть >= 0");
        }
        List<Booking> bookings = bookingService.getAll(userId, state, from, size);

        return bookings.stream()
                .map(bookingMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingResponseDto> getAllByOwner(@RequestHeader(user) int userId,
                                                  @RequestParam(defaultValue = "ALL") String state,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "99") int size) {
        if (from < 0 || size < 1) {
            log.info("Получены неверные значения size = " + size + ", from = " + from);
            throw new BadRequestException("Параметры пагинации должны быть >= 0");
        }
        List<Booking> bookings = bookingService.getAllByOwner(userId, state, from, size);

        return bookings.stream()
                .map(bookingMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }
}