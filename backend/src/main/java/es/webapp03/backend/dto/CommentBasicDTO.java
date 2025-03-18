package es.webapp03.backend.dto;

import java.time.LocalDate;

public record CommentBasicDTO(    
    Long id,
    LocalDate createdDate,
    String text) {
}
