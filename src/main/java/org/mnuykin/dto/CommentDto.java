package org.mnuykin.dto;

public record CommentDto(
        Integer id,
        String text,
        Integer postId) {
}