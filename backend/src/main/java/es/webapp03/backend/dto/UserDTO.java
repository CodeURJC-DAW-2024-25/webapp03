package es.webapp03.backend.dto;

import java.sql.Blob;
import java.util.List;

public record UserDTO(    
    Long id,
	String name,
    String email,
    boolean image,
    Blob imageFile,
	List<String> roles) {
}