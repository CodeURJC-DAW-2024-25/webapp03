package es.webapp03.backend.dto;

import java.time.LocalDate;

public record CommentDTO(    
    Long id,
	CourseBasicDTO course,
    UserBasicDTO user,
    LocalDate createdDate,
    String text) {
}
