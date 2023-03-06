package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@AllArgsConstructor
@Slf4j
public class BookingService {
    final BookingRepository bookingStorage;
    final ItemRepository itemStorage;

    public Booking add(Booking booking, int userId) {
        Item item = itemStorage.findById(booking.getItem())
                .orElseThrow(() -> new NotFoundException("Не удалось найти вещь для брони"));
        if (!item.getAvailable()) {
            throw new BadRequestException("Вещь недоступна для бронирования");
        }
        booking.setUser(userId);
        return bookingStorage.save(booking);
    }
}
