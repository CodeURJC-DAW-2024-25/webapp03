
package es.webapp03.backend.dto;

import java.util.List;

public record CourseInputDTO(
        String title,
        String description,
        List<String> tags) {
}