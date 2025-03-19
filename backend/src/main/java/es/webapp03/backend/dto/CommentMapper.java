package es.webapp03.backend.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.webapp03.backend.model.Comment;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    CommentBasicDTO toDTO(Comment comment);

    List<CommentBasicDTO> toDTOs(Collection<Comment> comments);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "course", ignore = true)
    Comment toDomain(CommentBasicDTO commentDTO);
}