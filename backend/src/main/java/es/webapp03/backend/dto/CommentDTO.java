package es.webapp03.backend.dto;

import java.time.LocalDate;

public record CommentDTO(
        Long id,
        CourseBasicDTO course,
        UserBasicDTO user,
        LocalDate createdDate,
        String text) {

    // Simplified constructor
    public CommentDTO {
        course = course != null ? course : new CourseBasicDTO(null, null, null, false, 0, null, null);
        user = user != null ? user : new UserBasicDTO(null, null, null);
    }
}