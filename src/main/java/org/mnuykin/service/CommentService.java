package org.mnuykin.service;

import org.mnuykin.model.Comment;
import java.util.List;

public interface CommentService {
    public List<Comment> findComments(Long postId);
    public Comment getComment(Long id);
    public Comment saveComment(Long postId, Comment comment);
    public Comment updeteComment(Long id, Long postId, Comment comment);
    public void deleteComment(Long id, Long postId);
}
