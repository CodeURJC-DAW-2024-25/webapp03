package es.webapp03.backend.dto;

import org.mapstruct.Mapper;

import es.webapp03.backend.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserBasicDTO toBasicDTO(User user);

    UserDTO toDTO(User user); // <-- Agregamos este método para el UserDTO
}
