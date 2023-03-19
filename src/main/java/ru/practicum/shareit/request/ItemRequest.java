package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotBlank
    @NotNull
    String description;
    @Column(name = "requester_id")
    @NotNull
    int requesterId;
    LocalDateTime created;
}
