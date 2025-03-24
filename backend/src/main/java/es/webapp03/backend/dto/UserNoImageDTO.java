package es.webapp03.backend.dto;

import java.util.List;

public record UserNoImageDTO(
    Long id,
    String name,
        String email,
        String password,
    List<CourseBasicDTO> courses,
    List<CommentBasicDTO> comments,
    List<String> roles) {
}
