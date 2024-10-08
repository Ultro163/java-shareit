package ru.practicum.shareit.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class CommentMapperTest {

    private CommentMapper commentMapper;
    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        commentMapper = Mappers.getMapper(CommentMapper.class);

        User user = new User();
        user.setId(1L);
        user.setName("John Doe");

        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");

        comment = new Comment();
        comment.setId(1L);
        comment.setText("Great item!");
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        commentDto = CommentDto.builder().build();
        commentDto.setId(1L);
        commentDto.setText("Great item!");
        commentDto.setItemId(1L);
        commentDto.setAuthorName("John Doe");
        commentDto.setCreated(LocalDateTime.now());
    }

    @Test
    void testMapToComment() {
        Comment result = commentMapper.mapToComment(commentDto);

        assertNotNull(result);
        assertEquals(commentDto.getId(), result.getId());
        assertEquals(commentDto.getText(), result.getText());
        assertNull(result.getAuthor());
        assertNull(result.getItem());
    }

    @Test
    void testMapToCommentDto() {
        CommentDto result = commentMapper.mapToCommentDto(comment);

        assertNotNull(result);
        assertEquals(comment.getId(), result.getId());
        assertEquals(comment.getText(), result.getText());
        assertEquals(comment.getAuthor().getName(), result.getAuthorName());
        assertEquals(comment.getItem().getId(), result.getItemId());
        assertEquals(comment.getCreated(), result.getCreated());
    }

    @Test
    void testMapToComment_Null() {
        Comment result = commentMapper.mapToComment(null);
        assertNull(result);
    }

    @Test
    void testMapToCommentDto_Null() {
        CommentDto result = commentMapper.mapToCommentDto(null);
        assertNull(result);
    }
}