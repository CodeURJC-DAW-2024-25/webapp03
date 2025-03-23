package es.webapp03.backend.dto;

import java.sql.Blob;
import java.util.List;

public record CourseBasicDTO(    
    Long id,
    String title,
	String description,
    boolean image,
    Blob imageFile,
    int numberOfUsers,
    List<String> tags) {
}