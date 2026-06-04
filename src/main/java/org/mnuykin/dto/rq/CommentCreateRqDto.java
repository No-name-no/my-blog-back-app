package org.mnuykin.dto.rq;

import jakarta.validation.constraints.NotNull;

public record CommentCreateRqDto (
        @NotNull
        String text,
        @NotNull
        Long postId) {
}