package es.webapp03.backend.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.webapp03.backend.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserBasicDTO toBasicDTO(User user);

    UserDTO toDTO(User user);

    @Mapping(target = "password", source = "encodedPassword")
    UserProfileDTO toProfileDTO(User user);


    
}
