package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
            select b from Booking b
            left join b.item i
            where (b.booker.id = ?2 and b.id = ?1)
            or (i.owner.id = ?2 and b.id = ?1)
            """)
    Optional<Booking> getUserBookingById(long bookingId, long userId);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(long bookerId, BookingStatus status);

    @Query("""
            select b from Booking b
            where (b.booker.id = ?1 and b.status = ?2)
            and (b.start < ?3 and b.end >= ?3)
            """)
    List<Booking> findByBookerIdAndCurrentTimeOrderByStartDesc(Long bookerId, BookingStatus status, LocalDateTime currentTime);

    List<Booking> findAllByBookerIdOrderByStartDesc(long bookerId);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long bookerId, LocalDateTime end);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long bookerId, LocalDateTime start);

    @Query("""
            select b from Booking b
            left join b.item i
            where (i.owner.id = ?1 and b.status = ?2)
            order by b.start desc
            """)
    List<Booking> findByOwnerIdAndStatusOrderByStartDesc(long ownerId, BookingStatus status);

    @Query("""
            select b from Booking b
            left join b.item i
            where (i.owner.id = ?1 and b.status = ?2)
            and (b.start < ?3 and b.end >= ?3)
            order by b.start desc
            """)
    List<Booking> findByOwnerIdAndCurrentTimeOrderByStartDesc(long ownerId, BookingStatus status, LocalDateTime currentTime);

    @Query("""
            select b from Booking b
            left join b.item i
            where (i.owner.id = ?1)
            order by b.start desc
            """)
    List<Booking> findAllByOwnerIdOrderByStartDesc(long ownerId);

    @Query("""
            select b from Booking b
            left join b.item i
            where (i.owner.id = ?1 and b.end < ?2)
            order by b.start desc
            """)
    List<Booking> findAllByOwnerIdAndEndBeforeOrderByStartDesc(long ownerId, LocalDateTime end);

    @Query("""
            select b from Booking b
            left join b.item i
            where (i.owner.id = ?1 and b.start > ?2)
            order by b.start desc
            """)
    List<Booking> findAllByOwnerIdAndStartAfterOrderByStartDesc(long ownerId, LocalDateTime start);

    @Query("""
            select b from Booking b
            left join b.item i
            where (i.id = ?1 and b.end < ?2)
            order by b.end desc
            limit 1
            """)
    Optional<Booking> findLastBookingForItem(long itemId, LocalDateTime localDateTime);

    @Query("""
            select b from Booking b
            left join b.item i
            where (i.id = ?1 and b.start > ?2)
            order by b.start
            limit 1
            """)
    Optional<Booking> findNextBookingForItem(long itemId, LocalDateTime localDateTime);
}