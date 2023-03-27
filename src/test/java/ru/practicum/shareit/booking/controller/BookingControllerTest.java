package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hamcrest.Matchers;
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
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.status.Status;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    public void updateTest() throws Exception {
        Mockito.when(mockBookingService.updateStatus(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean()))
                .thenReturn(new Booking(1,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1),
                        1,
                        Status.WAITING,
                        1));
        Mockito.when(mockBookingMapper.toBookingResponseDto(Mockito.any(Booking.class)))
                .thenReturn(new BookingResponseDto(1,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1),
                        null,
                        null,
                        Status.APPROVED));
        mockMvc.perform(patch("/bookings/1?approved=true")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(header, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("APPROVED")));
    }

    @Test
    public void getTest() throws Exception {
        Mockito.when(mockBookingService.get(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(new Booking(1,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1),
                        1,
                        Status.WAITING,
                        1));
        Mockito.when(mockBookingMapper.toBookingResponseDto(Mockito.any(Booking.class)))
                .thenReturn(new BookingResponseDto(1,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1),
                        null,
                        null,
                        Status.APPROVED));
        mockMvc.perform(get("/bookings/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void getAllThrowExceptionTest() throws Exception {
        mockMvc.perform(get("/bookings?from=-1&size=20")
                .header(header, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof BadRequestException));
    }

    @Test
    public void getAllTest() throws Exception {
        Booking b1 = new Booking(1, null, null, 1, Status.WAITING, 1);
        Booking b2 = new Booking(2, null, null, 2, Status.APPROVED, 1);
        List<Booking> bookings = List.of(b1, b2);
        Mockito.when(mockBookingService.getAll(Mockito.anyInt(),
                Mockito.anyString(),
                Mockito.anyInt(),
                Mockito.anyInt()))
                .thenReturn(bookings);

        mockMvc.perform(get("/bookings")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(header, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));

    }
}