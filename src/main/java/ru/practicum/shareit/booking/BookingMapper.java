package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingMapper {

    ItemService itemService;
    UserService userService;

    public BookingResponseDto toBookingResponseDto(Booking booking) {
        Item item = itemService.find(booking.getItemId());
        User booker = userService.get(booking.getBookerId());
        return new BookingResponseDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                new BookingResponseDto.Item(item.getId(), item.getName(), item.getDescription()),
                new BookingResponseDto.User(booker.getId(), booker.getName()),
                booking.getStatus()
        );
    }

    public Booking toBooking(BookingRequestDto bookingRequestDto) {
        Booking booking = new Booking();
        booking.setStart(bookingRequestDto.getStart());
        booking.setEnd(bookingRequestDto.getEnd());
        booking.setItemId(bookingRequestDto.getItemId());
        booking.setBookerId(bookingRequestDto.getBookerId());
        booking.setStatus(bookingRequestDto.getStatus());
        return booking;
    }
}
