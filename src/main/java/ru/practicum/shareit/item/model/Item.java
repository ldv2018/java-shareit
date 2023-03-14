package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

/**
 * TODO Sprint add-controllers.
 */
@Entity
@Table(schema = "public", name = "items")
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults (level = AccessLevel.PRIVATE)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    int id;
    @Column(name = "item_name")
    String name;
    @Column(name = "description")
    String description;
    @Column(name = "available")
    Boolean available;
    @Column(name = "item_owner")
    int owner;
    @Column(name = "request")
    int request;
}
