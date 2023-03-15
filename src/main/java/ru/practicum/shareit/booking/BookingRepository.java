package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.status.Status;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByBookerIdAndItemIdAndEndIsBefore(int bookerId, int itemId, LocalDateTime date);

    List<Booking> findByBookerIdOrderByStartDesc(int bookerId);

    List<Booking> findByBookerIdAndStatusEqualsOrderByStartDesc(int bookerId, Status status);

    List<Booking> findByBookerIdAndEndIsBeforeAndStatusEqualsOrderByStartDesc(
            int bookerId, LocalDateTime end, Status status);

    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(
            int bookerId, LocalDateTime start);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
            int bookerId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.itemId = ?1 " +
            "AND b.end <= ?2 " +
            "OR (b.start <= ?2 AND b.end >= ?2) " +
            "AND b.status = ?3 ORDER BY b.end DESC")
    List<Booking> findLastBookingsByItemId(int id, LocalDateTime dateTime, Status status);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.itemId = ?1 " +
            "AND b.start >= ?2 " +
            "AND b.status = ?3 ORDER BY b.end ASC")
    List<Booking> findNextBookingsByItemId(int id, LocalDateTime dateTime, Status status);

    List<Booking> findByItemIdInOrderByStartDesc(List<Integer> itemIds);

    List<Booking> findByItemIdInAndStatusEqualsOrderByStartDesc(List<Integer> itemIds, Status status);

    List<Booking> findByItemIdInAndEndIsBeforeAndStatusEqualsOrderByStartDesc(
            List<Integer> itemIds, LocalDateTime end, Status status);

    List<Booking> findByItemIdInAndStartIsAfterOrderByStartDesc(
            List<Integer> itemIds, LocalDateTime start);

    List<Booking> findByItemIdInAndStartBeforeAndEndIsAfterOrderByStartDesc(
            List<Integer> itemIds, LocalDateTime start, LocalDateTime end);
}
