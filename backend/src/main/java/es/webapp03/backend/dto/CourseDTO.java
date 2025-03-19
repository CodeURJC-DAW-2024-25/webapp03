package es.webapp03.backend.dto;

import java.util.List;

public record CourseDTO(    
    Long id,
    String title,
	String description,
    boolean image,
    int numberOfUsers,
    List<String> tags,
    List<UserBasicDTO> users,
    List<MaterialBasicDTO> materials,
    List<CommentBasicDTO> comments) {
}
