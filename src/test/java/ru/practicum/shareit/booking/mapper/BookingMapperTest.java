package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.status.Status;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class BookingMapperTest {
    @Mock
    private ItemService mockItemService;
    @Mock
    private UserService mockUserService;
    private BookingMapper bookingMapper;
    private Booking booking;
    private User user;
    private Item item;

    @BeforeEach
    void init() {
        bookingMapper = new BookingMapper(mockItemService, mockUserService);
        booking = new Booking(1, LocalDateTime.now(), LocalDateTime.MAX, 1, Status.APPROVED, 1);
        user = new User(1, "name", "email@email.com");
        item = new Item(1, "name", "description", true, 1, 2);
    }

    @Test
    public void toBookingResponseDtoTest() {
        Mockito.when(mockItemService.find(Mockito.anyInt()))
                .thenReturn(item);
        Mockito.when(mockUserService.get(Mockito.anyInt()))
                .thenReturn(user);
        BookingResponseDto bookingResponseDto= bookingMapper.toBookingResponseDto(booking);
        Assertions.assertEquals(bookingResponseDto.getId(), 1);
        Assertions.assertEquals(bookingResponseDto.getStart(), booking.getStart());
        Assertions.assertEquals(bookingResponseDto.getItem().getName(), "name");
        Assertions.assertEquals(bookingResponseDto.getBooker().getName(), "name");
    }
}