package org.mnuykin.dto.rs;

import java.util.List;

public record PageRsDto(
        List<PostRsDto> posts,
        Boolean hasPrev,
        Boolean hasNext,
        Integer lastPage) {
}