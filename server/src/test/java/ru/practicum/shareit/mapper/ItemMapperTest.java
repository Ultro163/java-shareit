package ru.practicum.shareit.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ItemMapperTest {

    private ItemMapper itemMapper;
    private Item item;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        itemMapper = Mappers.getMapper(ItemMapper.class);

        User user = new User();
        user.setId(1L);
        user.setName("John Doe");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);

        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequest(itemRequest);

        itemDto = ItemDto.builder().build();
        itemDto.setId(1L);
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);
        itemDto.setRequestId(1L);
    }

    @Test
    void testMapToItemDto() {
        ItemDto result = itemMapper.mapToItemDto(item);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
        assertEquals(item.getRequest().getId(), result.getRequestId());
    }

    @Test
    void testMapToItem() {
        Item result = itemMapper.mapToItem(itemDto);

        assertNotNull(result);
        assertEquals(itemDto.getId(), result.getId());
        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());
        assertEquals(itemDto.getAvailable(), result.getAvailable());
        assertNull(result.getRequest());
        assertNull(result.getOwner());
    }

    @Test
    void testMapToItemWithBookingsDto() {
        ItemWithBookingsDto result = itemMapper.mapToItemWithBookingsDto(item);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
    }

    @Test
    void testMapToItemDto_Null() {
        ItemDto result = itemMapper.mapToItemDto(null);
        assertNull(result);
    }

    @Test
    void testMapToItem_Null() {
        Item result = itemMapper.mapToItem(null);
        assertNull(result);
    }

    @Test
    void testMapToItemWithBookingsDto_Null() {
        ItemWithBookingsDto result = itemMapper.mapToItemWithBookingsDto(null);
        assertNull(result);
    }
}