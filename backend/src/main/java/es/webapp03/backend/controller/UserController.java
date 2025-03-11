package es.webapp03.backend.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Controller;

import es.webapp03.backend.service.UserService;
import es.webapp03.backend.model.Course;
import es.webapp03.backend.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();

        if (principal != null) {
            model.addAttribute("logged", true);
            model.addAttribute("userName", principal.getName());
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
        } else {
            model.addAttribute("logged", false);
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String password) {

        try {
            // Verificar si el usuario ya existe
            if (userService.findByEmail(email) != null) {
                return "registererror";
            }

            // Encriptar contraseña
            String encodedPassword = passwordEncoder.encode(password);

            // Crear y guardar usuario con rol "USER" (sin imagen)
            User newUser = new User(name, email, encodedPassword, null, "USER");
            userService.save(newUser);
            // Redirigir a la página de inicio
            return "redirect:/";
        } catch (Exception e) {
            return "registererror";
        }
    }

    @GetMapping("/admin/users")
    public String showAdminUsers(Model model, @RequestParam(defaultValue = "0") int page) {
     {
        int pageSize = 3; // Número de cursos por página
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<User> usersPage = userService.findAll(pageable);

        model.addAttribute("users", usersPage.getContent()); // Solo los cursos de la página actual
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usersPage.getTotalPages());

        return "adminUsers";
    }
    }

    @GetMapping("/deleteuser/{id}")
    public String deleteUser(@PathVariable long id) {
        if (userService.existsById(id)) {
            userService.deleteById(id);
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/profile/image")
    public ResponseEntity<Object> getProfileImage(Principal principal) throws SQLException {
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            if (user != null && user.getImageFile() != null) {
                Resource file = new InputStreamResource(user.getImageFile().getBinaryStream());
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                        .contentLength(user.getImageFile().length())
                        .body(file);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/edit_profile")
    public String showEditProfile(Model model, Principal principal) {
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            if (user != null) {
                model.addAttribute("user", user);
                return "modify_profile";
            }
        }
        return "redirect:/profile_page";
    }

    @PostMapping("/edit_profile")
    public String editProfile(@RequestParam String name,
                              @RequestParam String email,
                              @RequestParam(required = false) String password,
                              @RequestParam(required = false) MultipartFile image,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              Principal principal) throws IOException {

        if (principal != null) {
            User user = userService.findByEmail(principal.getName());

            if (user != null) {
                user.setName(name);

                boolean emailChanged = !user.getEmail().equals(email);
                user.setEmail(email);

                if (password != null && !password.isEmpty()) {
                    user.setEncodedPassword(passwordEncoder.encode(password));
                }

                if (image != null && !image.isEmpty()) {
                    user.setImage(BlobProxy.generateProxy(image.getInputStream(), image.getSize()));
                }

                userService.save(user);

                if (emailChanged) {
                    request.getSession().invalidate();
                    SecurityContextHolder.clearContext();
                    return "redirect:/login";
                }

                return "redirect:/profile_page";
            }
        }
        return "redirect:/edit_profile";
    }

    @GetMapping("/admin/users/load")
	public String loadMoreUser(@RequestParam int page, Model model) {
		int pageSize = 3; // Número de cursos por página
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<User> userPage = userService.findAll(pageable);

		model.addAttribute("users", userPage.getContent());
		return "fragments/userList"; // Devuelve un fragmento de HTML con los cursos
	}
}

