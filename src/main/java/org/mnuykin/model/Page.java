package org.mnuykin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Page {
    private List<Post> content;
    private Integer countPosts;
    private Integer pageNumber;
    private Integer pageSize;
}