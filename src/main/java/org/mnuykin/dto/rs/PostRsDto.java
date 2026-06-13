package org.mnuykin.dto.rs;

import java.util.List;

public record PostRsDto(
        Long id,
        String title,
        String text,
        List<String>tags,
        Integer likesCount,
        Integer commentCount) {
}