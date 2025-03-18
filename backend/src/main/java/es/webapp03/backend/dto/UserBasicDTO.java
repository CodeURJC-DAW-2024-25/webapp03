package es.webapp03.backend.dto;

import java.util.List;

public record UserBasicDTO(    
    Long id,
	String name,
    String email,
    boolean image,
	List<String> roles) {
}
