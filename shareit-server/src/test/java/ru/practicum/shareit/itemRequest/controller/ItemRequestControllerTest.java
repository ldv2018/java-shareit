package ru.practicum.shareit.itemRequest.controller;

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
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestMessageDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ItemRequestService mockItemRequestService;
    @Autowired
    ObjectMapper objectMapper;
    final String header = "X-Sharer-User-Id";

    @Test
    public void addIfRequestBodyIsNullTest() throws Exception {
        mockMvc.perform(post("/requests")
                    .header(header, 1)
                    .content(objectMapper.writeValueAsString(null))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addTest() throws Exception {
        ItemRequest ir = new ItemRequest(1,
                "desc",
                1,
                LocalDateTime.now().minusDays(1));
        Mockito.when(mockItemRequestService.add(Mockito.any(ItemRequest.class), Mockito.anyInt()))
                .thenReturn(ir);
        ItemRequestMessageDto irRequest = new ItemRequestMessageDto(1,
                "desc",
                1,
                null);
        mockMvc.perform(post("/requests")
                        .header(header, 1)
                        .content(objectMapper.writeValueAsString(irRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.created", Matchers.notNullValue()))
                .andExpect(jsonPath("$.requesterId", Matchers.is(1)));
    }

    @Test
    public void getIfItemRequestForUserIsEmptyTest() throws Exception {
        Mockito.when(mockItemRequestService.get(Mockito.anyInt()))
                .thenReturn(new HashMap<>());
        mockMvc.perform(get("/requests")
                    .header(header, 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    public void getTest() throws Exception {
        Map<ItemRequest, List<Item>> itemRequestAndItemAnswer = new HashMap<>();
        ItemRequest ir = new ItemRequest(1,
                "description",
                1,
                LocalDateTime.now());
        List<Item> items = List.of(
                new Item(1, "name1", "description1", true, 1, 1),
                new Item(2, "name2", "description2", true, 2, 1)
        );
        itemRequestAndItemAnswer.put(ir, items);
        Mockito.when(mockItemRequestService.get(Mockito.anyInt()))
                .thenReturn(itemRequestAndItemAnswer);
        mockMvc.perform(get("/requests")
                    .header(header, 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)));
    }

/*    @Test
    public void getAllIfBadPageParameters() throws Exception {
        mockMvc.perform((get("/requests/all?from=-1&size=2"))
                    .header(header, 1)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform((get("/requests/all?from=2&size=-1"))
                        .header(header, 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }*/

    @Test
    public void getAllTest() throws Exception {
        Map<ItemRequest, List<Item>> itemRequestAndItemAnswer = new HashMap<>();
        ItemRequest ir = new ItemRequest(1,
                "description",
                1,
                LocalDateTime.now());
        ItemRequest ir2 = new ItemRequest(2,
                "ir2",
                2,
                LocalDateTime.now());
        List<Item> items = List.of(
                new Item(1, "name1", "description1", true, 1, 1),
                new Item(2, "name2", "description2", true, 2, 1)
        );
        List<Item> items2 = List.of(
                new Item(3, "name3", "description3", true, 2, 2),
                new Item(4, "name4", "description4", true, 2, 2)
        );
        itemRequestAndItemAnswer.put(ir, items);
        itemRequestAndItemAnswer.put(ir2,items2);

        Mockito.when(mockItemRequestService.getAll(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()))
                        .thenReturn(itemRequestAndItemAnswer);

        mockMvc.perform(get("/requests/all?from=1&size=2")
                .header(header, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    public void getByRequestIdTest() throws Exception {
        Map<ItemRequest, List<Item>> itemRequestAndItemAnswer = new HashMap<>();
        ItemRequest ir = new ItemRequest(1,
                "description",
                1,
                LocalDateTime.now());
        List<Item> items = List.of(
                new Item(1, "name1", "description1", true, 1, 1),
                new Item(2, "name2", "description2", true, 2, 1)
        );
        itemRequestAndItemAnswer.put(ir, items);

        Mockito.when(mockItemRequestService.get(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(itemRequestAndItemAnswer);

        mockMvc.perform(get("/requests/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(header, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", Matchers.notNullValue()));
    }
}