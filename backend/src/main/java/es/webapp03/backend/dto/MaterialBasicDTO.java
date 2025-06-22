package es.webapp03.backend.dto;

public record MaterialBasicDTO(
                Long id,
                String name,
                String type,
                String url,
                CourseBasicDTO course) {
}
