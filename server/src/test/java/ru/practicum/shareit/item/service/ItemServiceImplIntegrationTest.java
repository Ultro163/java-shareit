package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.erorr.exception.EntityNotFoundException;
import ru.practicum.shareit.erorr.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @MockBean
    private BookingService bookingServiceImpl;

    @Autowired
    private UserRepository userRepository;

    private User owner;
    private Item item;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        userRepository.save(owner);

        item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Item Description");
        item.setAvailable(true);
        item.setOwner(owner);
        itemRepository.save(item);
    }

    @Test
    void testAddItem() {
        ItemDto itemDto = ItemDto.builder().build();
        itemDto.setName("New Item");
        itemDto.setDescription("New Item Description");
        itemDto.setAvailable(true);

        Item savedItem = itemService.addItem(owner.getId(), itemDto);
        assertNotNull(savedItem.getId());
        assertEquals("New Item", savedItem.getName());
    }

    @Test
    void testGetAllOwnerItems() {
        List<ItemWithBookingsDto> items = itemService.getAllOwnerItems(owner.getId());
        assertEquals(1, items.size());
        assertEquals(item.getName(), items.getFirst().getName());
    }

    @Test
    void testGetItem() {
        ItemWithBookingsDto itemDto = itemService.getItem(owner.getId(), item.getId());
        assertEquals(item.getName(), itemDto.getName());
    }

    @Test
    void testUpdateItem() {
        ItemDto itemDto = ItemDto.builder().build();
        itemDto.setName("Updated Item");
        itemDto.setDescription("Updated Item Description");

        Item updatedItem = itemService.updateItem(owner.getId(), item.getId(), itemDto);
        assertEquals("Updated Item", updatedItem.getName());
    }

    @Test
    void testAddComment() {
        List<Booking> testList = List.of(new Booking());
        when(bookingServiceImpl.getBookingsForComment(item.getId(), owner.getId()))
                .thenReturn(testList);
        CommentDto commentDto = CommentDto.builder().build();
        commentDto.setText("This is a comment");

        Comment comment = itemService.addComment(owner.getId(), item.getId(), commentDto);
        assertNotNull(comment.getId());
        assertEquals("This is a comment", comment.getText());
    }

    @Test
    void testGetItemNotFound() {
        long invalidItemId = 999L;
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> itemService.getItem(owner.getId(), invalidItemId));
        assertEquals("Item not found", exception.getMessage());
    }

    @Test
    void testUpdateItemNotFound() {
        long invalidItemId = 999L;
        ItemDto itemDto = ItemDto.builder().build();
        itemDto.setName("Should Fail");

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> itemService.updateItem(owner.getId(), invalidItemId, itemDto));
        assertEquals("Item not found", exception.getMessage());
    }

    @Test
    void testAddCommentWithoutBooking() {
        CommentDto commentDto = CommentDto.builder().build();
        commentDto.setText("Comment without booking");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> itemService.addComment(owner.getId(), item.getId(), commentDto));
        assertEquals("No completed bookings found", exception.getMessage());
    }

    @Test
    void testAddItemNotOwner() {
        User anotherUser = new User();
        anotherUser.setName("Another User");
        anotherUser.setEmail("another@example.com");
        anotherUser.setId(999L);

        ItemDto itemDto = ItemDto.builder().build();
        itemDto.setName("New Item");
        itemDto.setDescription("New Item Description");
        itemDto.setAvailable(true);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> itemService.addItem(anotherUser.getId(), itemDto));
        assertEquals("User with id 999 not found", exception.getMessage());
    }
}