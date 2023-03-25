package ru.practicum.shareit.itemRequest.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestEntityManager
public class ItemRequestRepositoryTest {

    @Autowired
    ItemRequestRepository itemRequestRepositoryTest;

    @Autowired
    UserRepository userRepositoryTest;

    @Test
    public void findAllByRequesterIdOrderByCreatedIfEmptyTest() {
        List<ItemRequest> itemRequests = itemRequestRepositoryTest.findAllByRequesterIdOrderByCreated(1);

        Assertions.assertTrue(itemRequests.isEmpty());
    }

    @Test
    public void findAllByRequesterIdOrderByCreatedIfNoСoincidenceTest() {
        userRepositoryTest.save(new User(1, "name", "email@imail.com"));
        userRepositoryTest.save(new User(2, "name", "name@imail.com"));
        itemRequestRepositoryTest.save(new ItemRequest(1,
                "description",
                userRepositoryTest.findAll().get(0).getId(),
                LocalDateTime.now()));
        itemRequestRepositoryTest.save(new ItemRequest(2,
                "description",
                userRepositoryTest.findAll().get(0).getId(),
                LocalDateTime.now()));
        List<ItemRequest> itemRequests = itemRequestRepositoryTest.findAllByRequesterIdOrderByCreated(3);

        Assertions.assertTrue(itemRequests.isEmpty());
    }

    @Test
    public void findAllByRequesterIdOrderByCreatedTest() {
        userRepositoryTest.save(new User(1, "name", "email@imail.com"));
        userRepositoryTest.save(new User(2, "name", "name@imail.com"));
        itemRequestRepositoryTest.save(new ItemRequest(1,
                "description",
                userRepositoryTest.findAll().get(0).getId(),
                LocalDateTime.now()));
        itemRequestRepositoryTest.save(new ItemRequest(2,
                "description",
                userRepositoryTest.findAll().get(1).getId(),
                LocalDateTime.now()));
        List<ItemRequest> itemRequests = itemRequestRepositoryTest
                .findAllByRequesterIdOrderByCreated(
                        userRepositoryTest.findAll().get(0).getId()
                );

        Assertions.assertEquals(itemRequests.size(), 1);
    }

    @Test
    public void findAllIfNoСoincidenceTest() {
        userRepositoryTest.save(new User(1, "name", "email@imail.com"));
        itemRequestRepositoryTest.save(new ItemRequest(1,
                "description",
                userRepositoryTest.findAll().get(0).getId(),
                LocalDateTime.now()));
        itemRequestRepositoryTest.save(new ItemRequest(2,
                "description",
                userRepositoryTest.findAll().get(0).getId(),
                LocalDateTime.now()));
        Pageable pageable = PageRequest.of(0, 1);
        Page<ItemRequest> itemRequests = itemRequestRepositoryTest
                .findAll(
                        userRepositoryTest.findAll().get(0).getId(),
                        pageable
                );

        Assertions.assertTrue(itemRequests.getContent().isEmpty());
    }

    @Test
    public void findAllTest() {
        userRepositoryTest.save(new User(1, "name", "email@imail.com"));
        itemRequestRepositoryTest.save(new ItemRequest(1,
                "description",
                userRepositoryTest.findAll().get(0).getId(),
                LocalDateTime.now()));
        itemRequestRepositoryTest.save(new ItemRequest(2,
                "description",
                userRepositoryTest.findAll().get(0).getId(),
                LocalDateTime.now()));
        Pageable pageable = PageRequest.of(0, 2);
        Page<ItemRequest> itemRequests = itemRequestRepositoryTest.findAll(99, pageable);

        Assertions.assertFalse(itemRequests.getContent().isEmpty());
        Assertions.assertEquals(itemRequests.getContent().size(), 2);
    }
}
