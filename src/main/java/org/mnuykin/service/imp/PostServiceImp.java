package org.mnuykin.service.imp;

import org.mnuykin.model.Post;
import org.mnuykin.service.PostService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImp implements PostService {
    @Override
    public List<Post> findPosts(String search, Integer pageNumber, Integer pageSize) {
        return List.of();
    }

    @Override
    public Post getPost(Long id) {
        return null;
    }

    @Override
    public Post savePost(Post post) {
        return null;
    }

    @Override
    public Post updatePost(Long id, Post post) {
        return null;
    }

    @Override
    public void deletePost(Long id) {

    }

    @Override
    public Integer addLike(Long id) {
        return 0;
    }

    @Override
    public Long getCountPost() {
        return 0L;
    }
}
