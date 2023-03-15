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
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.status.Status.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@AllArgsConstructor
@Slf4j
public class BookingService {
    final BookingRepository bookingStorage;
    final ItemRepository itemStorage;
    final UserRepository userStorage;

    public Booking add(Booking booking, int userId) {
        throwIfUserNotExist(userId);
        Item item = itemStorage.findById(booking.getItemId())
                .orElseThrow(() -> new NotFoundException("Не удалось найти вещь для брони"));
        if (!item.getAvailable()) {
            throw new BadRequestException("Вещь с id " + item.getId() + " недоступна для бронирования");
        }
        if (item.getOwner() == userId) {
            throw new NotFoundException("Вещь " + item.getId() + " не может быть забронирована владельцем");
        }
        booking.setBookerId(userId);
        booking.setStatus(WAITING);
        log.info("Бронирование добавлено");

        return bookingStorage.save(booking);
    }

    public Booking updateStatus(int userId, int bookingId, boolean approved) {
        Booking booking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирования не существует"));
        Item item = itemStorage.findById(booking.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        if (item.getOwner() != userId) {
            throw new NotFoundException("Пользователь с id " + userId + " не является владельцем");
        } else if (booking.getStatus() == APPROVED) {
            throw new BadRequestException("Бронирование с id " + bookingId + " уже подтверждено");
        } else {
            booking.setStatus(approved ? APPROVED : REJECTED);
        }
        log.info("Бронирование обновлено");

        return bookingStorage.save(booking);
    }

    public Booking get(int userId, int bookingId) {
        Booking booking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирования не существует"));
        Item item = itemStorage.findById(booking.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        if (item.getOwner() == userId || booking.getBookerId() == userId) {
            log.info("получено бронирование");
            return booking;
        } else {
            log.warn("доступ пользователя к бронированию запрещен");
            throw new NotFoundException("у пользователя id = " + userId + " нет доступа к бронированию");
        }
    }

    public List<Booking> getAll(int userId, String state) {
        List<Booking> bookings;
        LocalDateTime dateTime = LocalDateTime.now();
        throwIfUserNotExist(userId);

        switch (state) {
            case "ALL":
                bookings = bookingStorage.findByBookerIdOrderByStartDesc(userId);
                break;
            case "CURRENT":
                bookings = bookingStorage.findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                        userId,
                        dateTime,
                        dateTime);
                break;
            case "PAST":
                bookings = bookingStorage.findByBookerIdAndEndIsBeforeAndStatusEqualsOrderByStartDesc(
                        userId,
                        dateTime,
                        APPROVED);
                break;
            case "FUTURE":
                bookings = bookingStorage.findByBookerIdAndStartIsAfterOrderByStartDesc(
                        userId,
                        dateTime);
                break;
            case "WAITING":
                bookings = bookingStorage.findByBookerIdAndStatusEqualsOrderByStartDesc(
                        userId,
                        WAITING);
                break;
            case "REJECTED":
                bookings = bookingStorage.findByBookerIdAndStatusEqualsOrderByStartDesc(
                        userId,
                        REJECTED);
                break;
            default:
                log.error("запрошен некорректный статус");
                throw new BadRequestException("Unknown state: " + state);
        }
        log.info("получен список бронирований");

        return bookings;
    }

    public List<Booking> getAllByOwner(int userId, String state) {
        List<Integer> ownerItems = itemStorage.getAllByOwnerOrderByIdAsc(userId)
                .stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        List<Booking> bookings;
        LocalDateTime dateTime = LocalDateTime.now();
        throwIfUserNotExist(userId);

        switch (state) {
            case "ALL":
                bookings = bookingStorage.findByItemIdInOrderByStartDesc(ownerItems);
                break;
            case "CURRENT":
                bookings = bookingStorage.findByItemIdInAndStartBeforeAndEndIsAfterOrderByStartDesc(
                        ownerItems,
                        dateTime,
                        dateTime);
                break;
            case "PAST":
                bookings = bookingStorage.findByItemIdInAndEndIsBeforeAndStatusEqualsOrderByStartDesc(
                        ownerItems,
                        dateTime,
                        APPROVED);
                break;
            case "FUTURE":
                bookings = bookingStorage.findByItemIdInAndStartIsAfterOrderByStartDesc(
                        ownerItems,
                        dateTime);
                break;
            case "WAITING":
                bookings = bookingStorage.findByItemIdInAndStatusEqualsOrderByStartDesc(
                        ownerItems,
                        WAITING);
                break;
            case "REJECTED":
                bookings = bookingStorage.findByItemIdInAndStatusEqualsOrderByStartDesc(
                        ownerItems,
                        REJECTED);
                break;
            default:
                log.error("запрошен некорректный статус");
                throw new BadRequestException("Unknown state: " + state);
        }
        log.info("получен список бронирований");

        return bookings;
    }

    public Booking getNextBookingByItemId(int itemId) {
        List<Booking> bookings = bookingStorage
                .findNextBookingsByItemId(itemId, LocalDateTime.now(), APPROVED);
        if (!bookings.isEmpty()) {
            log.info("получено следующее бронирование");
            return bookings.get(0);
        } else {
            return null;
        }
    }

    public Booking getLastBookingByItemId(int itemId) {
        List<Booking> bookings = bookingStorage
                .findLastBookingsByItemId(itemId, LocalDateTime.now(), APPROVED);
        if (!bookings.isEmpty()) {
            log.info("получено последнее бронирование");
            log.info("количество последних бронирований = " + bookings.size());
            if (bookings.size() == 1) {
                log.info("Вернули первое. start = " + bookings.get(0).getStart() + "; end = " + bookings.get(0).getEnd());
                return bookings.get(0);
            } else {
                log.info("Первое. start = " + bookings.get(0).getStart() + "; end = " + bookings.get(0).getEnd());
                log.info("Вернули второе. start = " + bookings.get(1).getStart() + "; end = " + bookings.get(1).getEnd());
                return bookings.get(1);
            }
        } else {
            return null;
        }
    }

    private void throwIfUserNotExist(int id) {
        User user = userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователя " + id + " не существует"));
    }
}
