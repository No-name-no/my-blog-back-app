package org.mnuykin.mapper;

import org.mapstruct.Mapper;
import org.mnuykin.dto.CommentDto;
import org.mnuykin.model.Comment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDto toDto (Comment comment);
    Comment toModel (CommentDto commentDto);
    List<CommentDto> toDtoList (List<Comment> commnetList);
}