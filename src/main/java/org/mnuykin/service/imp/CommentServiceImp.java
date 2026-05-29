package org.mnuykin.service.imp;

import org.mnuykin.dto.CommnetDto;
import org.mnuykin.model.Comment;
import org.mnuykin.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImp implements CommentService {
    @Override
    public List<Comment> findComments(Long postId) {
        return List.of();
    }

    @Override
    public Comment getComment(Long id) {
        return null;
    }

    @Override
    public Comment saveComment(Long postId, Comment comment) {
        return null;
    }

    @Override
    public Comment updeteComment(Long id, Long postId, Comment comment) {
        return null;
    }

    @Override
    public void deleteComment(Long id, Long postId) {

    }
}