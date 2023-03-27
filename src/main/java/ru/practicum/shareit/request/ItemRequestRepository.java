package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {

    List<ItemRequest> findAllByRequesterIdOrderByCreated(int id);

    @Query("SELECT i FROM ItemRequest i" +
            " WHERE i.requesterId <> ?1")
    Page<ItemRequest> findAll(int userId, Pageable pageable);
}
