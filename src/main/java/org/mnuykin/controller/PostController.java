package org.mnuykin.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.mnuykin.dto.rq.CommentCreateRqDto;
import org.mnuykin.dto.rq.CommentUpdateRqDto;
import org.mnuykin.dto.rq.PostCreateRqDto;
import org.mnuykin.dto.rq.PostUpdateRqDto;
import org.mnuykin.dto.rs.CommentRsDto;
import org.mnuykin.dto.rs.PageRsDto;
import org.mnuykin.dto.rs.PostRsDto;
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
    public ResponseEntity<PageRsDto> posts(
            @RequestParam("search") String search,
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageSize") Integer pageSize){
        Page page = postService.findPage(postMapper.toPostFilter(search), pageNumber, pageSize);
        return ResponseEntity.ok(postMapper.toDto(page));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostRsDto> post(@PathVariable("postId") Long id){
        return ResponseEntity.ok(postMapper.toDto(postService.getPost(id)));
    }

    @PostMapping
    public ResponseEntity<PostRsDto> create(@Valid @RequestBody PostCreateRqDto postDto){
        return ResponseEntity.ok(postMapper.toDto(postService.savePost(postMapper.toModel(postDto))));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostRsDto> update(@PathVariable("postId") Long id,
                                            @Valid @RequestBody PostUpdateRqDto postDto){
        return ResponseEntity.ok(postMapper.toDto(postService.updatePost(id, postMapper.toModel(postDto))));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable("postId") Long id){
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{postId}/likes")
    public ResponseEntity<Integer> create(@PathVariable("postId") Long id){
        return ResponseEntity.ok(postService.addLike(id));
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentRsDto>> getComments(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(commentMapper.toDtoList(commentService.findComments(postId)));
    }

    @GetMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentRsDto> getComment(@PathVariable("postId") Long postId,
                                                   @PathVariable("commentId") Long commentId) {
        return ResponseEntity.ok(commentMapper.toDto(commentService.getComment(postId, commentId)));
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentRsDto> createComment(
            @PathVariable("postId") Long postId,
            @Valid @RequestBody CommentCreateRqDto comment) {

        Comment createdComment = commentMapper.toModel(comment);
        createdComment.setPostId(postId);

        return ResponseEntity.ok(commentMapper.toDto(commentService.saveComment(postId, createdComment)));
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentRsDto> updateComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CommentUpdateRqDto comment) {

        Comment updateComment = commentMapper.toModel(comment);
        return ResponseEntity.ok(commentMapper.toDto(commentService.updeteComment(commentId, postId, updateComment)));
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId,
                                              @PathVariable("postId") Long postId) {
        commentService.deleteComment(postId, commentId);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{postId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addPostImage(@PathVariable("postId") Long postId,
                                             @NotNull @RequestParam("image") MultipartFile file) {
        postFileService.upload(postId, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}/image")
    public ResponseEntity<Resource> getPostImage(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(postFileService.download(postId));
    }
}