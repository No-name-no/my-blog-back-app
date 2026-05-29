package org.mnuykin.mapper;

import org.mapstruct.Mapper;
import org.mnuykin.dto.CommnetDto;
import org.mnuykin.model.Comment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommnetDto toDto (Comment comment);
    Comment toModel (CommnetDto commnetDto);
    List<CommnetDto> toDtoList (List<Comment> commnetList);
    List<Comment> toModel (List<CommnetDto> commnetDtoList);
}