package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingMessageDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.validator.Validator;
import ru.practicum.shareit.exception.BadRequestException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
@Slf4j
public class BookingController {

	static final String HEADER_REQUEST = "X-Sharer-User-Id";

	private final BookingClient bookingClient;

	private final Validator<BookingMessageDto> bookingDtoValidator;

	@PostMapping
	public ResponseEntity<Object> add(@Valid @RequestBody BookingMessageDto dto,
									  @RequestHeader(HEADER_REQUEST) int userId) {
		log.info("Добавление бронирования {} от пользователя {}", dto, userId);
		bookingDtoValidator.throwIfNotValid(dto);
		return bookingClient.postBooking(dto, userId);
	}

	@PatchMapping(value = "/{bookingId}")
	public ResponseEntity<Object> patch(@PathVariable @NotNull int bookingId,
										@RequestHeader(HEADER_REQUEST) int userId,
										@RequestParam @NotNull boolean approved) {
		log.info("Обновление бронирования {} от пользователя {} подтверждение {}", bookingId, userId, approved);
		return bookingClient.patchBooking(userId, bookingId, approved);
	}

	@GetMapping(value = "/{bookingId}")
	public ResponseEntity<Object> getBooking(@PathVariable @NotNull int bookingId,
											 @RequestHeader(HEADER_REQUEST) int userId) {
		log.info("Получение бронирования {} от пользователя {}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@GetMapping()
	public ResponseEntity<Object> findAllByStateBooker(@RequestHeader(HEADER_REQUEST) int userId,
													   @RequestParam(name = "state", defaultValue = "ALL") String state,
													   @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
													   @RequestParam(name = "size", defaultValue = "99") @Positive Integer size) {
		log.info("Получение бронирования {} state {} from {} size {}", userId, state, from,size);
		try {
			return bookingClient.findAllByStateBooker(userId, BookingState.valueOf(state), from, size);
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("Unknown state: " + state);
		}
	}


	@GetMapping(value = "/owner")
	public ResponseEntity<Object> findAllByStateOwner(@RequestHeader(HEADER_REQUEST) int userId,
													  @RequestParam(name = "state", defaultValue = "ALL") String state,
													  @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
													  @RequestParam(name = "size", defaultValue = "99") @Positive Integer size) {
		log.info("Получение бронирования от пользователя {} state {} from {} size {}", userId, state, from,size);
		try {
			return bookingClient.findAllByStateOwner(userId, BookingState.valueOf(state), from, size);
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("Unknown state: " + state);
		}
	}
}
