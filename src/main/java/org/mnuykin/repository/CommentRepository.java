package org.mnuykin.repository;

import org.mnuykin.model.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    List<Comment> findAll(Long postId);
    Optional<Comment> get(Long postId, Long commentId);
    Optional<Comment> save(Comment comment);
    Optional<Comment> update(Comment comment);
    Integer delete(Long postId, Long commentId);
}
