package org.mnuykin.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mnuykin.dto.rq.PostCreateRqDto;
import org.mnuykin.dto.rq.PostUpdateRqDto;
import org.mnuykin.dto.rs.PageDto;
import org.mnuykin.dto.rs.PostDto;
import org.mnuykin.model.Page;
import org.mnuykin.model.Post;
import org.mnuykin.model.PostFilter;

import java.util.HashSet;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "likesCount", constant="0")
    @Mapping(target = "commentCount", constant="0")
    Post toModel (PostCreateRqDto postCreateRqDto);

    @Mapping(target = "likesCount", ignore = true)
    @Mapping(target = "commentCount", ignore = true)
    Post toModel (PostUpdateRqDto postUpdateRqDto);

    PostDto toDto (Post post);

    @Mapping(target = "text", source = "text", qualifiedByName = "customTextMapping")
    PostDto toDtoForList (Post post);

    @IterableMapping(qualifiedByName = "toDtoForList")
    List<PostDto> toDtoList (List<Post> postList);

    @Mapping(target = "posts", source = "content")
    @Mapping(target = "lastPage", ignore = true) //expression = "java(page.getCountPosts().longValue()/page.getPageSize())")
    @Mapping(target = "hasPrev", ignore = true) //expression = "java(page.getPageNumber() > 1)")
    @Mapping(target = "hasNext", ignore = true) //expression = "java(page.getCountPosts()/page.getPageSize() > page.getPageNumber())")
    PageDto toDto (Page page);

    @Named("toDtoForList")
    default PostDto toDtoWithMapping(Post post) {
        return toDtoForList(post);
    }

    @Named("customTextMapping")
    default String customTextMapping(String text){
        final int textMaxSize = 128;
        return text != null && text.length() > textMaxSize ? text.substring(textMaxSize) : text;
    }

    default PostFilter toPostFilter(String search){
        if (search == null || search.isEmpty())
            return new PostFilter();

        StringBuilder searchFilter = new StringBuilder();
        HashSet<String> tags = new HashSet<>();
        for (String temp: search.trim().split("\\s+")){
            temp = temp.trim();
            if(temp.startsWith("#")){
                tags.add(temp.substring(1));
            } else if (!temp.isBlank()){
                if (searchFilter.isEmpty()) {
                    searchFilter.append(temp);
                } else {
                    searchFilter.append(" ").append(temp);
                }
            }
        }

        return new PostFilter(searchFilter.toString(), tags);
    }
}
