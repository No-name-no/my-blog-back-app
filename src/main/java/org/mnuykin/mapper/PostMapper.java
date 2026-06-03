package org.mnuykin.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mnuykin.dto.rq.PostCreateRqDto;
import org.mnuykin.dto.rq.PostUpdateRqDto;
import org.mnuykin.dto.rs.PageRsDto;
import org.mnuykin.dto.rs.PostRsDto;
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

    PostRsDto toDto (Post post);

    @Mapping(target = "text", source = "text", qualifiedByName = "customTextMapping")
    PostRsDto toDtoForList (Post post);

    @IterableMapping(qualifiedByName = "toDtoForList")
    List<PostRsDto> toDtoList (List<Post> postList);

    @Mapping(target = "posts", source = "content")
    @Mapping(target = "lastPage", expression = "java(getLastPage(page.getCountPosts(), page.getPageSize()))")
    @Mapping(target = "hasPrev", expression = "java(page.getPageNumber() > 1)")
    @Mapping(target = "hasNext", expression = "java(getLastPage(page.getCountPosts(), page.getPageSize()) > page.getPageNumber())")
    PageRsDto toDto (Page page);

    @Named("toDtoForList")
    default PostRsDto toDtoWithMapping(Post post) {
        return toDtoForList(post);
    }

    @Named("customTextMapping")
    default String customTextMapping(String text){
        final int textMaxSize = 128;
        return text != null && text.length() > textMaxSize ? text.substring(textMaxSize) : text;
    }

    default Integer getLastPage(Integer countPosts, Integer pageSize){
        if(countPosts == null || pageSize == null || pageSize == 0)
            return 0;

        return (countPosts + pageSize - 1)/pageSize;
    }

    default PostFilter toPostFilter(String search){
        StringBuilder searchFilter = new StringBuilder();
        HashSet<String> tags = new HashSet<>();

        if (search == null || search.isEmpty())
            return new PostFilter("", tags);

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
