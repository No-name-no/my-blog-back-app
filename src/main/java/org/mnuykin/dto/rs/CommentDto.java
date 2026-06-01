package org.mnuykin.dto.rs;

public record CommentDto(
        Long id,
        String text,
        Integer postId) {
}