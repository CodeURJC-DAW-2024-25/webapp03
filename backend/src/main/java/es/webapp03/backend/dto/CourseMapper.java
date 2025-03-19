package es.webapp03.backend.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.webapp03.backend.model.Course;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseBasicDTO toDTO(Course course);

    List<CourseBasicDTO> toDTOs(Collection<Course> courses);

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "materials", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Course toDomain(CourseBasicDTO courseDTO);
}