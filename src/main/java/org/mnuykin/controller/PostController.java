package org.mnuykin.controller;

import jakarta.validation.Valid;
import org.mnuykin.dto.rq.CommentCreateRqDto;
import org.mnuykin.dto.rq.CommentUpdateRqDto;
import org.mnuykin.dto.rq.PostCreateRqDto;
import org.mnuykin.dto.rq.PostUpdateRqDto;
import org.mnuykin.dto.rs.CommentDto;
import org.mnuykin.dto.rs.PageDto;
import org.mnuykin.dto.rs.PostDto;
import org.mnuykin.mapper.CommentMapper;
import org.mnuykin.mapper.PostMapper;
import org.mnuykin.model.Comment;
import org.mnuykin.model.Page;
import org.mnuykin.service.CommentService;
import org.mnuykin.service.PostFileService;
import org.mnuykin.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    final private CommentService commentService;
    final private PostService postService;
    final private PostFileService postFileService;
    final private CommentMapper commentMapper;
    final private PostMapper postMapper;

    @Autowired
    PostController(PostService postService, CommentService commentService, PostFileService postFileService,
            PostMapper postMapper, CommentMapper commentMapper){
        this.postService = postService;
        this.commentService = commentService;
        this.postFileService = postFileService;
        this.commentMapper = commentMapper;
        this.postMapper = postMapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageDto> posts(
            @RequestParam("search") String search,
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageSize") Integer pageSize){
        Page page = postService.findPage(postMapper.toPostFilter(search), pageNumber, pageSize);
        return ResponseEntity.ok(postMapper.toDto(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> post(@PathVariable("id") Long id){
        return ResponseEntity.ok(postMapper.toDto(postService.getPost(id)));
    }

    @PostMapping
    public ResponseEntity<PostDto> create(@Valid @RequestBody PostCreateRqDto postDto){
        return ResponseEntity.ok(postMapper.toDto(postService.savePost(postMapper.toModel(postDto))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> update(@PathVariable("id") Long id, @Valid @RequestBody PostUpdateRqDto postDto){
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
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentMapper.toDtoList(commentService.findComments(postId)));
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRqDto comment) {

        Comment createdComment = commentMapper.toModel(comment);
        createdComment.setPostId(postId);

        return ResponseEntity.ok(commentMapper.toDto(commentService.saveComment(postId, createdComment)));
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRqDto comment) {

        Comment updateComment = commentMapper.toModel(comment);
        return ResponseEntity.ok(commentMapper.toDto(commentService.updeteComment(commentId, postId, updateComment)));
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId,
                                              @PathVariable("postId") Long postId) {
        commentService.deleteComment(postId, commentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{postId}/image")
    public ResponseEntity<Void> addPostImage(@PathVariable Long postId,
                                             @RequestParam("file") MultipartFile file) {
        postFileService.upload(postId, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}/image")
    public ResponseEntity<Resource> getPostImage(@PathVariable Long postId) {
        return ResponseEntity.ok(postFileService.download(postId));
    }

}