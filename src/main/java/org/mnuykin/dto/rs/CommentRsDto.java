package org.mnuykin.dto.rs;

public record CommentRsDto(
        Long id,
        String text,
        Integer postId) {
}