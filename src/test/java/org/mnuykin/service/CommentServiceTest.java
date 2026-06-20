package org.mnuykin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mnuykin.model.Comment;
import org.mnuykin.repository.CommentRepository;
import org.mnuykin.repository.PostRepository;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@SpringBootTest
public class CommentServiceTest {
    private final Long postId = 1L;
    private final Long commentId = 1L;

    @MockitoBean
    private CommentRepository commentRepository;
    @MockitoBean
    private PostRepository postRepository;
    @Autowired
    private CommentService commentService;

    @BeforeEach
    void resetBeforeEach() {
        Mockito.reset(commentRepository);
        Mockito.reset(postRepository);
    }

    @Test
    void testFindComments() {
        List<Comment> comments = List.of(new Comment(1L, "Text1", postId),
                new Comment(2L, "Text2", postId));

        Mockito.when(commentRepository.findAll(postId)).thenReturn(comments);

        List<Comment> findComments = commentService.findComments(postId);
        assertNotNull(findComments);
        assertArrayEquals(findComments.toArray(), comments.toArray());
    }

    @Test
    void testGetComments() {
        Comment comment = new Comment(commentId, "Text", postId);
        Mockito.when(commentRepository.get(postId, commentId)).thenReturn(Optional.of(comment));

        Comment resultComment = commentService.getComment(postId, commentId);
        assertNotNull(resultComment);
        assertEquals(resultComment, comment);
    }

    @Test
    void testSaveComments() {
        final String text = "Text";
        Comment comment = new Comment(commentId, text, postId);
        Comment saveComment = new Comment(null, text, postId);

        Mockito.when(commentRepository.save(saveComment)).thenReturn(Optional.of(comment));

        Comment resultComment = commentService.saveComment(postId, saveComment);
        assertNotNull(resultComment);
        assertEquals(resultComment, comment);
    }

    @Test
    void testUpdateComments() {
        final String text = "Text";
        Comment comment = new Comment(commentId, text, postId);
        Comment updateComment = new Comment(commentId, text, postId);

        Mockito.when(commentRepository.update(updateComment)).thenReturn(Optional.of(comment));

        Comment resultComment = commentService.updeteComment(postId, commentId, new Comment(commentId, text, postId));
        assertNotNull(resultComment);
        assertEquals(resultComment, comment);
    }

    @Test
    void deleteComment(){
        Mockito.when(commentRepository.delete(postId, commentId)).thenReturn(1);
        commentService.deleteComment(commentId, postId);
    }
}
