package org.mnuykin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mnuykin.dto.rq.CommentCreateRqDto;
import org.mnuykin.dto.rq.CommentUpdateRqDto;
import org.mnuykin.dto.rs.CommentDto;
import org.mnuykin.model.Comment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    Comment toModel (CommentCreateRqDto commentDto);
    Comment toModel (CommentUpdateRqDto commentDto);

    CommentDto toDto (Comment comment);
    List<CommentDto> toDtoList (List<Comment> commnetList);
}