package org.mnuykin.service;

import org.mnuykin.dto.PostDto;
import org.mnuykin.dto.PostsDto;
import org.mnuykin.model.Post;

import java.util.List;

public interface PostService {
    public List<Post> findPosts(String search, Integer pageNumber, Integer pageSize);
    public Post getPost(Long id);
    public Post savePost(Post post);
    public Post updatePost(Long id, Post post);
    public void deletePost(Long id);
    public Integer addLike(Long id);
    public Long getCountPost();
}
