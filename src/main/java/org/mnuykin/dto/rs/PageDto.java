package org.mnuykin.dto.rs;

import java.util.List;

public record PageDto(
        List<PostDto> posts,
        Boolean hasPrev,
        Boolean hasNext,
        Long lastPage) {
}