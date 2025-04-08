package es.webapp03.backend.dto;

public record UserNoImageDTO(
    Long id,
    String name,
        String email,
        String password) {
}
