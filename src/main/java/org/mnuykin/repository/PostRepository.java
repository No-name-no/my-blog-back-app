package org.mnuykin.repository;

import org.mnuykin.model.Page;
import org.mnuykin.model.Post;
import org.mnuykin.model.PostFilter;

import java.util.Optional;

public interface PostRepository {
    Optional<Page> findPage(PostFilter postFilter, Integer pageNumber, Integer pageSize);
    Optional<Post> get(Long id);
    Optional<Post> save(Post post);
    Optional<Post> update(Post post);
    Integer delete(Long id);
    Integer addLike(Long id);
}
