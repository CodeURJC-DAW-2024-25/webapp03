package es.webapp03.backend.dto;

public record MaterialSimpleDTO(
        Long id,
        String name,
        String type,
        String url) {
}
