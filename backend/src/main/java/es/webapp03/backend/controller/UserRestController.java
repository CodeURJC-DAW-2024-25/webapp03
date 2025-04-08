package es.webapp03.backend.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import es.webapp03.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import es.webapp03.backend.dto.UserBasicDTO;
import es.webapp03.backend.dto.UserDTO;
import es.webapp03.backend.dto.UserNoImageDTO;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<UserBasicDTO> registerUser(@RequestBody UserNoImageDTO userNoImageDTO) {

        // Check if the user already exists
        if (userService.findByEmail(userNoImageDTO.email()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Encrypt password
        UserBasicDTO createdUserProfile = userService.registerUser(
                userNoImageDTO.name(),
                userNoImageDTO.email(),
                userNoImageDTO.password(),
                "USER");

        // Return success response with the created user
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserProfile);

    }

    @GetMapping("/")
    public Page<UserBasicDTO> getUsers(Pageable pageable) {
        return userService.findAllBasicUserDTO(pageable);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserBasicDTO> deleteUser(@PathVariable long id) {
        UserBasicDTO deletedUser = userService.findUserBasicDTOById(id);
        if (deletedUser != null) {
            userService.deleteById(id);

            return ResponseEntity.ok(deletedUser);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Object> getProfileImage(@PathVariable Long id, HttpServletRequest request) throws SQLException, IOException {

        // Get the authenticated user
        Principal principal = request.getUserPrincipal();
        UserDTO accessingUser = userService.findUserDTOByEmail(principal.getName());

        // Check if the user has permission (admin or their own profile)
        if (accessingUser == null || (!accessingUser.roles().contains("ADMIN") && !accessingUser.id().equals(id))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Return 403 Forbidden
        }

        // Get the user's profile image
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
    public ResponseEntity<UserBasicDTO> getUserProfile(@PathVariable Long id, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        UserDTO accesingUser = userService.findUserDTOByEmail(principal.getName());

        UserBasicDTO retievedUser = userService.findUserBasicDTOById(id);

        // Check if user has admin role
        if (accesingUser != null && (accesingUser.roles().contains("ADMIN") || accesingUser.id().equals(id))) {

            return ResponseEntity.ok(retievedUser);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editUserProfile(@PathVariable Long id, @RequestBody UserNoImageDTO userNoImageDTO, HttpServletRequest request) {

        // Get the authenticated user
        Principal principal = request.getUserPrincipal();
        UserDTO accessingUser = userService.findUserDTOByEmail(principal.getName());

        // Check if the user has permission (admin or their own profile)
        if (accessingUser == null || (!accessingUser.roles().contains("ADMIN") && !accessingUser.id().equals(id))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Return 403 Forbidden
        }

        // Check if the user exists
        UserBasicDTO existingUser = userService.findUserBasicDTOById(id);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Check if the email is already in use by another user
        if (userService.findByEmail(userNoImageDTO.email()) != null
                && !userNoImageDTO.email().equals(existingUser.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Update the user profile
        userService.updateUserProfile(id, userNoImageDTO.name(), userNoImageDTO.email(), userNoImageDTO.password());

        return ResponseEntity.ok("User profile updated");
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<String> updateProfileImage(@PathVariable Long id, @RequestParam MultipartFile imageFile, HttpServletRequest request) throws IOException {

        // Get the authenticated user
        Principal principal = request.getUserPrincipal();
        UserDTO accessingUser = userService.findUserDTOByEmail(principal.getName());

        // Check if the user has permission (admin or their own profile)
        if (accessingUser == null || (!accessingUser.roles().contains("ADMIN") && !accessingUser.id().equals(id))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); 
        }

        // Check if the user exists
        UserBasicDTO existingUser = userService.findUserBasicDTOById(id);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Update the profile image
        userService.updateUserProfileImage(id, imageFile.getInputStream(), imageFile.getSize());

        return ResponseEntity.ok("Profile image updated");
    }
}
