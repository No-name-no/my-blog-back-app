package org.mnuykin.dto;

import java.util.List;

public record PostDto(
        Integer id,
        String title,
        String text,
        List<String>tags,
        Integer likesCount,
        Integer commentCount) {
}
