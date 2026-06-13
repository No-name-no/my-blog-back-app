package org.mnuykin.dto.rq;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PostUpdateRqDto(
        @NotNull
        Long id,
        @NotNull
        @Size(max = 255)
        String title,
        @NotNull
        String text,
        @NotNull
        List<String>tags) {
}