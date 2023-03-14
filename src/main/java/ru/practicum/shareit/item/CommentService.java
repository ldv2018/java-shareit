package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE)
public class CommentService {
    final CommentRepository commentRepository;
    final UserRepository userRepository;
    final BookingRepository bookingRepository;

    public Comment add(int userId, int itemId, Comment comment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("Пользователь не найден"));
        comment.setAuthorId(userId);
        List<Booking> bookings = bookingRepository
                  .findByBookerIdAndItemIdAndEndIsBefore(userId, itemId, LocalDateTime.now());
        if (bookings.isEmpty()) {
            throw new BadRequestException("Пользователь не брал вещь в аренду");
        }
        comment.setItemId(itemId);
        comment.setCreated(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    @Transactional
    public List<Comment> findAllByItemId(int itemId) {
        return commentRepository.findAllByItemId(itemId);
    }
}
