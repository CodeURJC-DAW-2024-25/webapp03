package es.webapp03.backend.dto;

import java.sql.Blob;

public record UserProfileDTO(
        String name,
        String email,
        String password,
        Blob imageFile)

{
}
