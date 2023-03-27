package ru.practicum.shareit.request.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Entity
@Table(schema = "public", name = "requests")
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults (level = AccessLevel.PRIVATE)
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    int id;
    String description;
    @Column(name = "requester_id")
    int requesterId;
    LocalDateTime created;
}
