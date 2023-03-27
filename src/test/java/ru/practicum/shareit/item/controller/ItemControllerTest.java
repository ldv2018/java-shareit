package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.CommentService;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.status.Status;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemControllerTest {
    @MockBean
    ItemService itemService;
    @MockBean
    BookingService bookingService;
    @MockBean
    CommentService commentService;
    @MockBean
    UserService userService;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mvc;
    final String header = "X-Sharer-User-Id";
    final ItemDto itemDto = new ItemDto(1, "name", "description", true, 1, 1);
    final Item item = new Item(1, "name", "description", true, 1, 1);

    @Test
    public void saveItemTest() throws Exception {

        Mockito.when(itemService.add(Mockito.any(Item.class), Mockito.anyInt()))
                        .thenReturn(item);
        mvc.perform(post("/items")
                .header(header, 1)
                .content(mapper.writeValueAsString(itemDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), int.class));
    }

    @Test
    public void updateItemTest() throws Exception {
        Item updatedItem = new Item(1,
                "updatedName",
                "updatedDescription",
                true,
                1,
                1);
        Mockito.when(itemService.update(Mockito.any(Item.class),
                        Mockito.anyInt(),
                        Mockito.anyInt()))
                .thenReturn(updatedItem);
        mvc.perform(patch("/items/1")
                .header(header, 1)
                .content(mapper.writeValueAsString(updatedItem))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is(updatedItem.getName()), String.class));
    }

    @Test
    public void getItemTest() throws Exception {
        Mockito.when(itemService.find(Mockito.anyInt()))
                .thenReturn(item);
        Mockito.when(bookingService.getNextBookingByItemId(Mockito.anyInt()))
                .thenReturn(new Booking(1,
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(2),
                        1,
                        Status.APPROVED,
                        1));
        Mockito.when(bookingService.getLastBookingByItemId(Mockito.anyInt()))
                .thenReturn(new Booking(1,
                        LocalDateTime.now().minusDays(2),
                        LocalDateTime.now().minusDays(1),
                        1,
                        Status.APPROVED,
                        2));
        Mockito.when(userService.get(Mockito.anyInt()))
                .thenReturn(new User(20, "name", "name@emial.com"));
        Comment c1 = new Comment(1,
                "description",
                1,
                20,
                LocalDateTime.now().minusSeconds(60));
        Comment c2 = new Comment(2,
                "description2",
                1,
                20,
                LocalDateTime.now().minusSeconds(600));
        List<Comment> comments = new ArrayList<>();
        comments.add(c1);
        comments.add(c2);
        Mockito.when(commentService.findAllByItemId(Mockito.anyInt()))
                .thenReturn(comments);

        mvc.perform(get("/items/1")
                        .header(header, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is(item.getName()), String.class))
                .andExpect(jsonPath("$.comments", Matchers.hasSize(2)));
    }

    @Test
    public void findAllByOwnerTest() throws Exception {
        List<Item> items = new ArrayList<>();
        items.add(item);
        Mockito.when(itemService.findAllByUser(Mockito.anyInt(),
                Mockito.anyInt(),
                Mockito.anyInt()))
                .thenReturn(items);
        Mockito.when(bookingService.getNextBookingByItemId(Mockito.anyInt()))
                .thenReturn(new Booking(1,
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(2),
                        1,
                        Status.APPROVED,
                        1));
        Mockito.when(bookingService.getLastBookingByItemId(Mockito.anyInt()))
                .thenReturn(new Booking(1,
                        LocalDateTime.now().minusDays(2),
                        LocalDateTime.now().minusDays(1),
                        1,
                        Status.APPROVED,
                        2));
        mvc.perform(get("/items/")
                .header(header, 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    public void getByReviewTest() throws Exception {
        List<Item> items = new ArrayList<>();
        items.add(item);
        Mockito.when(itemService.findByReview(Mockito.anyString()))
                .thenReturn(items);
        mvc.perform(get("/items/search?text='desc'/")
                .header(header, 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)));
    }
}
