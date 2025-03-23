package es.webapp03.backend.dto;

import java.sql.Blob;

public record MaterialDTO (    
    Long id,
    String name,
    String type,
    String url,
    Blob file,
	CourseBasicDTO course) {
}
