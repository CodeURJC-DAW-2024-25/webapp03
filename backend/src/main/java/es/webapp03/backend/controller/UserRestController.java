package es.webapp03.backend.controller;

import java.io.IOException;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import es.webapp03.backend.service.UserService;
import es.webapp03.backend.dto.UserDTO;
import es.webapp03.backend.dto.UserNoImageDTO;
import es.webapp03.backend.model.User;
import es.webapp03.backend.dto.UserMapper;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/")
    public ResponseEntity<UserNoImageDTO> registerUser(@RequestBody UserNoImageDTO userNoImageDTO) {
        try {
            // Check if the user already exists
            if (userService.findByEmail(userNoImageDTO.email()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            // Encrypt password
            String encodedPassword = passwordEncoder.encode(userNoImageDTO.password());

            // Create and save user with role "USER" (without image)
            User newUser = new User(userNoImageDTO.name(), userNoImageDTO.email(), encodedPassword, null, "USER");
            userService.save(newUser);

            // Convert the saved user to UserProfileDTO
            UserNoImageDTO createdUserProfileDTO = userService.findUserProfileById(newUser.getId());

            // Return success response with the created user
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUserProfileDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/")
    public Page<UserNoImageDTO> showUsers(Pageable pageable) {
        return userService.findAllWithNoImage(pageable);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserNoImageDTO> deleteUser(@PathVariable long id) {
        UserDTO userDTO = userService.findUserById(id);
        if (userDTO != null) {
            userService.deleteById(id);
            UserNoImageDTO userNoImageDTO = userMapper.toNoImageDTO(userDTO);
            return ResponseEntity.ok(userNoImageDTO); // Devolver el recurso borrado
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
    public ResponseEntity<UserNoImageDTO> showUserProfile(@PathVariable Long id) {
        UserNoImageDTO userNoImageDTO = userService.findUserProfileById(id);

        if (userNoImageDTO != null) {
            return ResponseEntity.ok(userNoImageDTO);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editUserProfile(@PathVariable Long id, @RequestBody UserNoImageDTO userNoImageDTO) {
        
        return ResponseEntity.ok("User profile updated");
    }
}
