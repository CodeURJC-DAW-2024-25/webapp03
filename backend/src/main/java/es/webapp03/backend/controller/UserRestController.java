package es.webapp03.backend.controller;

import java.io.IOException;
import java.sql.SQLException;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import es.webapp03.backend.service.UserService;
import es.webapp03.backend.dto.UserBasicDTO;
import es.webapp03.backend.dto.UserDTO;
import es.webapp03.backend.dto.UserProfileDTO;
import es.webapp03.backend.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//Si necesito devolver sin contraseña puedo mmapear profile dto a basic
@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/")
    public ResponseEntity<UserProfileDTO> registerUser(@RequestBody UserProfileDTO userProfileDTO) {

        try {
            // Check if the user already exists
            if (userService.findByEmail(userProfileDTO.email()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            // Encrypt password
            String encodedPassword = passwordEncoder.encode(userProfileDTO.password());

            // Create and save user with role "USER" (without image)
            User newUser = new User(userProfileDTO.name(), userProfileDTO.email(), encodedPassword, null, "USER");
            userService.save(newUser);

            // Convert the saved user to UserProfileDTO
            UserProfileDTO createdUserProfileDTO = userService.findUserProfileById(newUser.getId());

            // Return success response with the created user
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUserProfileDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/") // Mirar paginacion en el material
    public Page<UserDTO> showUsers(Pageable pageable) { // recibir pageable
        return userService.findAll(pageable);
    }

    @DeleteMapping("/{id}") // Checkear respuesta borrado
    public ResponseEntity<UserDTO> deleteUser(@PathVariable long id) { // Checkear respuesta borrado
        UserDTO userDTO = userService.findUserById(id);
        if (userDTO != null) {
            userService.deleteById(id);
            return ResponseEntity.ok(userDTO); // Devolver el recurso borrado
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Object> getProfileImage(@PathVariable Long id) throws SQLException, IOException {
        UserDTO userDTO = userService.findUserById(id);
        if (userDTO != null && userDTO.imageFile() != null) {
            Resource file = new InputStreamResource(userDTO.imageFile().getBinaryStream());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(userDTO.imageFile().length())
                    .body(file);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDTO> showUserProfile(@PathVariable Long id) {
        UserProfileDTO userProfileDTO = userService.findUserProfileById(id);

        if (userProfileDTO != null) {
            return ResponseEntity.ok(userProfileDTO);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/{id}") // TODO logout logic

    public ResponseEntity<String> editUserProfile() {
            return null;

        
    }
}
