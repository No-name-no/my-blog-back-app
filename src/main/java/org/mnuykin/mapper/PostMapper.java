package org.mnuykin.mapper;

import org.mapstruct.Mapper;
import org.mnuykin.dto.PostDto;
import org.mnuykin.model.Post;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostDto toDto (Post post);
    Post toModel (PostDto postsDto);
    List<PostDto> toDtoList (List<Post> postList);
    List<Post> toModel (List<PostDto> postDtoList);
}
