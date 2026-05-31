package org.mnuykin.service;

import org.mnuykin.model.Page;
import org.mnuykin.model.Post;
import org.mnuykin.model.PostFilter;

public interface PostService {
    public Page findPage(PostFilter filter, Integer pageNumber, Integer pageSize);
    public Post getPost(Long id);
    public Post savePost(Post post);
    public Post updatePost(Long id, Post post);
    public void deletePost(Long id);
    public Integer addLike(Long id);
}
