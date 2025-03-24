package es.webapp03.backend.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.webapp03.backend.model.Comment;
import es.webapp03.backend.model.Course;
import es.webapp03.backend.model.Material;
import es.webapp03.backend.model.User;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseBasicDTO toDTO(Course course);

    List<CourseBasicDTO> toDTOs(Collection<Course> courses);

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "materials", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "imageFile", ignore = true)
    Course toDomain(CourseBasicDTO courseDTO);

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "materials", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Course toDomain(CourseDTO courseDTO);

    @Mapping(target = "users", source = "users")
    @Mapping(target = "materials", source = "materials")
    @Mapping(target = "comments", source = "comments")
    CourseDTO toFullDTO(Course course);

    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "encodedPassword", ignore = true)
    List<UserBasicDTO> mapUsers(Collection<User> users);

    @Mapping(target = "course", ignore = true)
    List<MaterialBasicDTO> mapMaterials(Collection<Material> materials);

    @Mapping(target = "course", ignore = true)
    @Mapping(target = "user", ignore = true)
    List<CommentBasicDTO> mapComments(Collection<Comment> comments);
}