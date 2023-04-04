package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.status.Status;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingServiceTest {
    BookingService bookingService;
    @Mock
    BookingRepository mockBookingRepository;
    @Mock
    ItemRepository mockItemRepository;
    @Mock
    UserRepository mockUserRepository;
    Booking booking;
    Item item;

    @BeforeEach
    void init() {
        bookingService = new BookingService(mockBookingRepository,
                mockItemRepository,
                mockUserRepository);
        booking = new Booking(1,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                1,
                Status.WAITING,
                1);
        item = new Item(1,
                "name",
                "description",
                true,
                1,
                1);
    }

    @Test
    public void addWithUserNotExistTest() {
        Mockito.when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.add(booking, 1)
        );
        Assertions.assertEquals("Пользователя 1 не существует", exception.getMessage());
    }

    @Test
    public void addWithItemNotExistTest() {
        Mockito.when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(new User(1, "name", "email@email.com")));
        Mockito.when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.add(booking, 1)
        );
        Assertions.assertEquals("Не удалось найти вещь для брони", exception.getMessage());
    }

    @Test
    public void addWithItemNotAvailableTest() {
        Mockito.when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(new User(1, "name", "email@email.com")));
        Mockito.when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(new Item(1,
                        "name",
                        "description",
                        false,
                        1,
                        1)));
        BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> bookingService.add(booking, 1)
        );
        Assertions.assertEquals("Вещь с id 1 недоступна для бронирования", exception.getMessage());
    }

    @Test
    public void addWithUserEqualsOwnerTest() {
        Mockito.when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(new User(1, "name", "email@email.com")));
        Mockito.when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(new Item(1,
                        "name",
                        "description",
                        true,
                        1,
                        1)));
        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.add(booking, 1)
        );
        Assertions.assertEquals("Вещь 1 не может быть забронирована владельцем", exception.getMessage());
    }

    @Test
    public void addTest() {
        Mockito.when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(new User(1, "name", "email@email.com")));
        Mockito.when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(new Item(1,
                        "name",
                        "description",
                        true,
                        2,
                        1)));
        Mockito.when(mockBookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(booking);
        Booking booking1 = bookingService.add(booking, 1);
        Assertions.assertEquals(booking1, booking);
    }

    @Test
    public void updateStatusWithBookingNotExistTest() {
        Mockito.when((mockBookingRepository.findById(Mockito.anyInt())))
                .thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.updateStatus(1, 1, true)
        );
        Assertions.assertEquals("Бронирования не существует", exception.getMessage());
    }

    @Test
    public void updateStatusWithApprovedStatusTest() {
        booking.setStatus(Status.APPROVED);
        Mockito.when(mockBookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(booking));
        Mockito.when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item));
        BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> bookingService.updateStatus(1, 1, true)
        );
        Assertions.assertEquals("Бронирование с id 1 уже подтверждено", exception.getMessage());
    }

    @Test
    public void updateStatusTest() {
        Mockito.when(mockBookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(booking));
        Mockito.when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item));
        Mockito.when(mockBookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(booking);
        Booking b = bookingService.updateStatus(1, 1, true);
        Assertions.assertEquals(b, booking);
    }

    @Test
    public void getWithBookingNotExistTest() {
        Mockito.when(mockBookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.get(1, 1)
        );
        Assertions.assertEquals("Бронирования не существует", exception.getMessage());
    }

    @Test
    public void getWithUserDeniedAccessToBookingTest() {
        Mockito.when(mockBookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(booking));
        Mockito.when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item));
        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.get(10, 1)
        );
        Assertions.assertEquals("у пользователя id = 10 нет доступа к бронированию", exception.getMessage());
    }

    @Test
    public void getTest() {
        Mockito.when(mockBookingRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(booking));
        Mockito.when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item));
        Booking booking1 = bookingService.get(1, 1);
        Assertions.assertEquals(booking, booking1);
    }
}
