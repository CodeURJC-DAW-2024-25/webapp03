package es.webapp03.backend.dto;

import java.util.List;

public record CourseBasicDTO(    
    Long id,
	String description,
    boolean image,
    int numberOfUsers,
    List<String> tags) {
}