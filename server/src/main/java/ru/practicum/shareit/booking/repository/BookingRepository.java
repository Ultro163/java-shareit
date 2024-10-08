package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    Optional<Booking> getUserBookingById(long bookingId, long bookerId);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(long bookerId, BookingStatus status);

    @Query("""
            select b from Booking b
            where b.booker.id = ?1
            and b.start < ?2 and b.end >= ?2
            order by b.start
            """)
    List<Booking> findAllByBookerIdAndCurrentTime(Long bookerId, LocalDateTime currentTime);

    Page<Booking> findAllByBookerIdOrderByStartDesc(long bookerId, Pageable page);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long bookerId, LocalDateTime end);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long bookerId, LocalDateTime start);

    @Query("""
            select b from Booking b
            left join b.item i
            where (i.owner.id = ?1 and b.status = ?2)
            order by b.start desc
            """)
    List<Booking> findAllByOwnerIdAndStatus(long ownerId, BookingStatus status);

    @Query("""
            select b from Booking b
            left join b.item i
            where i.owner.id = ?1
            and b.start < ?2 and b.end >= ?2
            order by b.start desc
            """)
    List<Booking> findAllByOwnerIdAndCurrentTime(long ownerId, LocalDateTime currentTime);

    @Query("""
            select b from Booking b
            left join b.item i
            where (i.owner.id = ?1)
            order by b.start desc
            """)
    Page<Booking> findAllByOwnerIdOrderByStartDesc(long ownerId, Pageable page);

    @Query("""
            select b from Booking b
            left join b.item i
            where (i.owner.id = ?1 and b.end < ?2)
            order by b.start desc
            """)
    List<Booking> findAllByOwnerIdAndEndBefore(long ownerId, LocalDateTime end);

    @Query("""
            select b from Booking b
            left join b.item i
            where (i.owner.id = ?1 and b.start > ?2)
            order by b.start desc
            """)
    List<Booking> findAllByOwnerIdAndStartAfter(long ownerId, LocalDateTime start);

    @Query("""
            select b from Booking b
            where (b.item.id = ?1 and b.start < ?2 and b.status != ?3)
            order by b.start desc
            limit 1
            """)
    Optional<Booking> findLastBookingForItem(long itemId, LocalDateTime localDateTime, BookingStatus status);

    @Query("""
            select b from Booking b
            where (b.item.id = ?1 and b.start > ?2 and b.status != ?3)
            order by b.start
            limit 1
            """)
    Optional<Booking> findNextBookingForItem(long itemId, LocalDateTime localDateTime, BookingStatus status);

    @Query("""
            select b from Booking b
            where (b.item.id = ?1 and b.booker.id = ?2 and b.end < ?3)
            """)
    List<Booking> findBookingForComment(long itemId, long bookerId, LocalDateTime localDateTime);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.item.id IN :itemIds and b.status != :status
            """)
    List<Booking> findByItemIdIn(@Param("itemIds") List<Long> itemIds, @Param("status") BookingStatus status);

}