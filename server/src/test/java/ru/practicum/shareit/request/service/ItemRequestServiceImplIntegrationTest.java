package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.erorr.exception.EntityNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplIntegrationTest {

    private final ItemRequestServiceImpl itemRequestService;
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private User user;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();

        user = new User();
        user.setName("User");
        user.setEmail("user@example.com");
        userRepository.save(user);

        itemRequest = new ItemRequest();
        itemRequest.setDescription("Need a tool");
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest);
    }

    @Test
    void testCreateItemRequest() {
        ItemRequestDto requestDto = ItemRequestDto.builder().build();
        requestDto.setDescription("Looking for a drill");

        ItemRequest createdRequest = itemRequestService.create(user.getId(), requestDto);
        assertNotNull(createdRequest.getId());
        assertEquals("Looking for a drill", createdRequest.getDescription());
    }

    @Test
    void testGetRequestByIdWithItems() {
        ItemRequestDto requestDto = itemRequestService.getRequestByIdWithItems(user.getId(), itemRequest.getId());
        assertEquals(itemRequest.getDescription(), requestDto.getDescription());
        assertTrue(requestDto.getItems().isEmpty());
    }

    @Test
    void testGetUserRequests() {
        ItemRequestDto requestDto = itemRequestService.getUserRequests(user.getId(), 0, 10).getFirst();
        assertEquals(itemRequest.getDescription(), requestDto.getDescription());
    }

    @Test
    void testGetAllRequests() {
        User user1 = new User();
        user1.setName("User1");
        user1.setEmail("user1@example.com");
        userRepository.save(user1);

        List<ItemRequestDto> requests = itemRequestService.getAllRequests(user1.getId(), 0, 10);
        assertEquals(1, requests.size());
        assertEquals(itemRequest.getDescription(), requests.getFirst().getDescription());
    }

    @Test
    void testGetRequestById() {
        ItemRequest foundRequest = itemRequestService.getRequestById(user.getId(), itemRequest.getId());
        assertEquals(itemRequest.getDescription(), foundRequest.getDescription());
    }

    @Test
    void testGetRequestByIdNotFound() {
        long invalidRequestId = 999L;
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> itemRequestService.getRequestByIdWithItems(user.getId(), invalidRequestId));
        assertEquals("Request not found", exception.getMessage());
    }

    @Test
    void testGetUserRequestsNotFound() {
        long invalidUserId = 999L;
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> itemRequestService.getUserRequests(invalidUserId, 0, 10));
        assertEquals("User with id 999 not found", exception.getMessage());
    }

    @Test
    void testCreateRequestForNonExistentUser() {
        long invalidUserId = 999L;
        ItemRequestDto requestDto = ItemRequestDto.builder().build();
        requestDto.setDescription("Looking for a saw");

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> itemRequestService.create(invalidUserId, requestDto));
        assertEquals("User with id 999 not found", exception.getMessage());
    }
}