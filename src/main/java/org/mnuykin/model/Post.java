package org.mnuykin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private Long id;
    private String title;
    private String text;
    private List<String> tags;
    private Integer likesCount;
    private Integer commentCount;
}
