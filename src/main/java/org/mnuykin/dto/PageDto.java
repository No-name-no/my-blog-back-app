package org.mnuykin.dto;

import java.util.List;

public record PageDto(
        List<PostDto> posts,
        Boolean hasPrev,
        Boolean hasNext,
        Long lastPage) {
}