package org.mnuykin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mnuykin.model.Page;
import org.mnuykin.model.Post;
import org.mnuykin.model.PostFilter;
import org.mnuykin.repository.PostRepository;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PostServiceTest {
    @MockitoBean
    private PostRepository postRepository;
    @Autowired
    private PostService postService;

    @BeforeEach
    void resetBeforeEach() {
        Mockito.reset(postRepository);
    }

    @Test
    void testFindPage(){
        List<Post> posts = List.of(new Post(1L, "title", "text", List.of("tag1"), 1, 0));
        Page page = new Page(posts, 2, 1, 1);
        PostFilter postFilter = new PostFilter("title", Set.of("#tag1"));

        Mockito.when(postRepository.findPage(postFilter, 1, 1)).thenReturn(Optional.of(page));

        Page resultPage = postService.findPage(postFilter, 1, 1);
        assertNotNull(resultPage);
        assertEquals(resultPage, page);
    }

    @Test
    void getPost(){
        Post post = new Post(1L, "title", "text", List.of("tag1"), 0, 0);
        Mockito.when(postRepository.get(post.getId())).thenReturn(Optional.of(post));

        Post resultPost = postService.getPost(post.getId());
        assertNotNull(resultPost);
        assertEquals(resultPost, post);
    }

    @Test
    void savePost(){
        Post post = new Post(null, "title", "text", List.of("tag1"), 0, 0);
        Post savePost = new Post(1L, "title", "text", List.of("tag1"), 0, 0);
        Mockito.when(postRepository.save(post)).thenReturn(Optional.of(savePost));

        Post resultPost = postService.savePost(post);
        assertNotNull(resultPost);
        assertEquals(resultPost, savePost);
    }

    @Test
    void updatePost(){
        Post post = new Post(1L, "title", "text", List.of("tag1"), 0, 0);
        Post updatePost = new Post(1L, "title", "text", List.of("tag1"), 0, 0);
        Mockito.when(postRepository.update(post)).thenReturn(Optional.of(updatePost));

        Post resultPost = postService.updatePost(post.getId(), post);
        assertNotNull(resultPost);
        assertEquals(resultPost, updatePost);
    }

    @Test
    void deletePost(){
        Mockito.when(postRepository.delete(1L)).thenReturn(1);
        postService.deletePost(1L);
    }

    @Test
    void addLike(){
        final long postId = 1L;
        final Integer like = 5;

        Mockito.when(postRepository.addLike(postId)).thenReturn(like);
        Integer result = postService.addLike(postId);
        assertNotNull(result);
        assertEquals(like, result);
    }
}
