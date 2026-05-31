package org.mnuykin.service.imp;

import org.mnuykin.model.Page;
import org.mnuykin.model.Post;
import org.mnuykin.model.PostFilter;
import org.mnuykin.repository.PostRepository;
import org.mnuykin.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PostServiceImp implements PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostServiceImp(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @Override
    public Page findPage(PostFilter filter, Integer pageNumber, Integer pageSize) {
        return postRepository.findPage(filter, pageNumber, pageSize).orElseThrow();
    }

    @Override
    public Post getPost(Long id) {
        return postRepository.get(id).orElseThrow();
    }

    @Override
    public Post savePost(Post post) {
        return postRepository.save(post).orElseThrow();
    }

    @Override
    public Post updatePost(Long id, Post post) {
        if(!Objects.equals(id, post.getId()))
            return null;

        return postRepository.update(post).orElseThrow();
    }

    @Override
    public void deletePost(Long id) {
        postRepository.delete(id);
    }

    @Override
    public Integer addLike(Long id) {
        return postRepository.addLike(id);
    }
}
