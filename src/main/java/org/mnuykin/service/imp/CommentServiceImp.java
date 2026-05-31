package org.mnuykin.service.imp;

import org.mnuykin.model.Comment;
import org.mnuykin.repository.CommentRepository;
import org.mnuykin.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CommentServiceImp implements CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImp (CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> findComments(Long postId) {
        return commentRepository.findAll(postId);
    }

    @Override
    public Comment getComment(Long postId, Long id) {
        return commentRepository.get(postId, id).orElseThrow(/*TODO*/);
    }

    @Override
    public Comment saveComment(Long postId, Comment comment) {
        if(!Objects.equals(postId, comment.getPostId()))
            return null;//TODO:error
        return commentRepository.save(comment).orElseThrow();
    }

    @Override
    public Comment updeteComment(Long id, Long postId, Comment comment) {
        if (!Objects.equals(id, comment.getId()) || !Objects.equals(postId, comment.getPostId()))
            return null;

        return commentRepository.update(comment).orElseThrow();
    }

    @Override
    public void deleteComment(Long id, Long postId) {
        commentRepository.delete(postId, id);
    }
}