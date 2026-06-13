package org.mnuykin.service.imp;

import org.mnuykin.model.Comment;
import org.mnuykin.repository.CommentRepository;
import org.mnuykin.repository.PostRepository;
import org.mnuykin.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CommentServiceImp implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentServiceImp (CommentRepository commentRepository, PostRepository postRepository){
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public List<Comment> findComments(Long postId) {
        return commentRepository.findAll(postId);
    }

    @Override
    public Comment getComment(Long postId, Long id) {
        return commentRepository.get(postId, id).orElseThrow();
    }

    @Override
    @Transactional
    public Comment saveComment(Long postId, Comment comment) {
        if(!Objects.equals(postId, comment.getPostId()))
            throw new RuntimeException("Разные инд. объектов");

        Comment saveComment = commentRepository.save(comment).orElseThrow();
        postRepository.addComment(postId);
        return saveComment;
    }

    @Override
    @Transactional
    public Comment updeteComment(Long id, Long postId, Comment comment) {
        if (!Objects.equals(id, comment.getId()) || !Objects.equals(postId, comment.getPostId()))
            throw new RuntimeException("Разные инд. объектов");
        return commentRepository.update(comment).orElseThrow();
    }

    @Override
    @Transactional
    public void deleteComment(Long id, Long postId) {
        int i = commentRepository.delete(postId, id);
        if (i > 0) {
            postRepository.deleteComment(postId);
        }
    }
}