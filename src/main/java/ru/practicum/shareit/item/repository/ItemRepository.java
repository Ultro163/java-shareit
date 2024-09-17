package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long OwnerId);

    @Query("""
            select i
            from Item as i
            where (i.name ilike ?1 or i.description ilike ?1)
            and i.available = true
            """)
    List<Item> searchItemsWithTextFilter(String text);


}