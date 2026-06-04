package org.mnuykin.dto.rq;

import jakarta.validation.constraints.NotNull;

public record CommentUpdateRqDto(
        @NotNull
        Long id,
        @NotNull
        String text,
        @NotNull
        Long postId) {
}