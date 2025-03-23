package es.webapp03.backend.dto;

import org.mapstruct.Mapper;

import es.webapp03.backend.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserBasicDTO toDTO(User user);

    

}