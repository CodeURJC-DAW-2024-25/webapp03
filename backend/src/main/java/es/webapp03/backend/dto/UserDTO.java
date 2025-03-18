package es.webapp03.backend.dto;

import java.util.List;

public record UserDTO(    
    Long id,
	String name,
    String email,
    boolean image,
    List<CourseBasicDTO> courses,
    List<CommentBasicDTO> comments,
	List<String> roles) {
}