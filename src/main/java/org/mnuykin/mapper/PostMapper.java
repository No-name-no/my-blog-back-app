package org.mnuykin.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mnuykin.dto.PageDto;
import org.mnuykin.dto.PostDto;
import org.mnuykin.model.Page;
import org.mnuykin.model.Post;
import org.mnuykin.model.PostFilter;

import java.util.HashSet;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostDto toDto (Post post);
    Post toModel (PostDto postsDto);

    @Mapping(target = "text", source = "text", qualifiedByName = "customTextMapping")
    PostDto toDtoForList (Post post);

    @IterableMapping(qualifiedByName = "toDtoForList")
    List<PostDto> toDtoList (List<Post> postList);

    @Mapping(target = "posts", source = "content")
    @Mapping(target = "lastPage", expression = "java(page.getCountPosts().longValue()/page.getPageSize())")
    @Mapping(target = "hasPrev", expression = "java(page.getPageNumber() > 1)")
    @Mapping(target = "hasNext", expression = "java(page.getCountPosts()/page.getPageSize() > page.getPageNumber())")
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
                tags.add(temp);
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
