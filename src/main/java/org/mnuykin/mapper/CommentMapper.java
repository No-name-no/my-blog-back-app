package org.mnuykin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mnuykin.dto.rq.CommentCreateRqDto;
import org.mnuykin.dto.rq.CommentUpdateRqDto;
import org.mnuykin.dto.rs.CommentRsDto;
import org.mnuykin.model.Comment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    Comment toModel (CommentCreateRqDto commentDto);
    Comment toModel (CommentUpdateRqDto commentDto);

    CommentRsDto toDto (Comment comment);
    List<CommentRsDto> toDtoList (List<Comment> commnetList);
}