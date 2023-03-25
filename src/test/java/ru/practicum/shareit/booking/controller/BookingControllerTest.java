package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingValidator;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.status.Status;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingControllerTest {
    @MockBean
    BookingService mockBookingService;
    @MockBean
    BookingValidator mockBookingValidator;
    @MockBean
    BookingMapper mockBookingMapper;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    final String header = "X-Sharer-User-Id";
    BookingResponseDto bookingResponseDto;
    BookingRequestDto bookingRequestDto;

    @BeforeEach
    void init() {
        bookingResponseDto = new BookingResponseDto(1,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                null,
                null,
                Status.WAITING);
        bookingRequestDto = new BookingRequestDto(1,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                1,
                1,
                Status.WAITING);
    }

    @Test
    public void addTest() throws Exception {
        Mockito.when(mockBookingMapper.toBooking(Mockito.any(BookingRequestDto.class)))
                .thenReturn(new Booking(1,
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(2),
                        1,
                        Status.WAITING,
                        1));
        Mockito.when(mockBookingService.add(Mockito.any(Booking.class), Mockito.anyInt()))
                .thenReturn(new Booking(1,
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(2),
                        1,
                        Status.WAITING,
                        1));
        Mockito.when(mockBookingMapper.toBookingResponseDto(Mockito.any(Booking.class)))
                        .thenReturn(bookingResponseDto);
        mockMvc.perform(post("/bookings")
                .header(header, 1)
                .content(objectMapper.writeValueAsString(bookingRequestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(bookingRequestDto.getId()), long.class));
    }


}
