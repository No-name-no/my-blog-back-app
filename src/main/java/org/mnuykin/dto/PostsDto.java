package org.mnuykin.dto;

import java.util.List;

public record PostsDto(
        List<PostDto> posts,
        Boolean hasPrev,
        Boolean hasNext,
        Long lastPage) {
}