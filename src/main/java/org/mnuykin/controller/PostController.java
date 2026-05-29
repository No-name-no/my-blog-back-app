package org.mnuykin.controller;

import org.mnuykin.dto.CommnetDto;
import org.mnuykin.dto.PostDto;
import org.mnuykin.dto.PostsDto;
import org.mnuykin.mapper.CommentMapper;
import org.mnuykin.mapper.PostMapper;
import org.mnuykin.model.Comment;
import org.mnuykin.model.Post;
import org.mnuykin.service.CommentService;
import org.mnuykin.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    final private CommentService commentService;
    final private PostService postService;
    final private CommentMapper commentMapper;
    final private PostMapper postMapper;

    @Autowired
    PostController(PostService postService, CommentService commentService,
            PostMapper postMapper, CommentMapper commentMapper){
        this.postService = postService;
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.postMapper = postMapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostsDto> posts(
            @RequestParam("search") String search,
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageSize") Integer pageSize){
        List<Post> posts = postService.findPosts(search, pageNumber, pageSize);
        Long countPosts = postService.getCountPost();
        Long lastPage = countPosts / pageSize;
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = pageNumber < lastPage;

        PostsDto postsDto = new PostsDto(postMapper.toDtoList(posts), hasPrev, hasNext, lastPage);
        return ResponseEntity.ok(postsDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> post(@PathVariable("id") Long id){
        return ResponseEntity.ok(postMapper.toDto(postService.getPost(id)));
    }

    @PostMapping
    public ResponseEntity<PostDto> create(@RequestBody PostDto postDto){
        return ResponseEntity.ok(postMapper.toDto(postService.savePost(postMapper.toModel(postDto))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> update(@PathVariable("id") Long id, @RequestBody PostDto postDto){
        return ResponseEntity.ok(postMapper.toDto(postService.updatePost(id, postMapper.toModel(postDto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/likes")
    public ResponseEntity<Integer> create(@PathVariable("id") Long id){
        return ResponseEntity.ok(postService.addLike(id));
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommnetDto>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentMapper.toDtoList(commentService.findComments(postId)));
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommnetDto> createComment(
            @PathVariable Long postId,
            @RequestBody CommnetDto comment) {

        Comment createdComment = commentMapper.toModel(comment);
        createdComment.setPostId(postId);

        return ResponseEntity.ok(commentMapper.toDto(commentService.saveComment(postId, createdComment)));
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommnetDto> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody CommnetDto comment) {

        Comment updateComment = commentMapper.toModel(comment);
        return ResponseEntity.ok(commentMapper.toDto(commentService.updeteComment(commentId, postId, updateComment)));
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
                                              @PathVariable("postId") Long postId) {
        commentService.deleteComment(postId, commentId);
        return ResponseEntity.noContent().build();
    }

}