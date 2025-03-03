package es.webapp03.backend.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Controller;

import es.webapp03.backend.repository.CommentRepository;
import es.webapp03.backend.repository.UserRepository;
import es.webapp03.backend.service.UserService;
import es.webapp03.backend.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

    @GetMapping("/register")
	public String showRegisterForm(Model model) {

		return "register";
	}

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestParam String name,
			@RequestParam String email,
			@RequestParam String password, HttpServletResponse response) {

		try {
			// Verificar si el usuario ya existe
			if (userRepository.findByEmail(email).isPresent()) {
				return ResponseEntity.badRequest().body("El email ya está en uso.");
			}

			// Encriptar contraseña
			String encodedPassword = passwordEncoder.encode(password);

			// Crear y guardar usuario con rol "USER" (sin imagen)
			User newUser = new User(name, email, encodedPassword, null, "USER");
			userRepository.save(newUser);
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(email, password));
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// Redirigir a la página de inicio
			response.sendRedirect("/");
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al registrar usuario: " + e.getMessage());
		}

	}

	@GetMapping("/admin/users")
	public String showAdminUsers(Model model) {
		List<User> users = userRepository.findByRoles("USER");
		model.addAttribute("users", users);
		return "adminUsers";
	}

	@GetMapping("/deleteuser/{id}")
	public String deleteUser(@PathVariable long id) {
    	if (userRepository.existsById(id)) {
        	userRepository.deleteById(id);
    	}
   		return "redirect:/admin/users";
	}

	@GetMapping("/profile/image")
	public ResponseEntity<Object> getProfileImage(Principal principal) throws SQLException {
    	if (principal != null) {
        	Optional<User> user = userRepository.findByEmail(principal.getName());
        	if (user.isPresent() && user.get().getImageFile() != null) {
            	Resource file = new InputStreamResource(user.get().getImageFile().getBinaryStream());
            	return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(user.get().getImageFile().length())
                    .body(file);
        	}
    	}
    	return ResponseEntity.notFound().build();
	}

	@GetMapping("/edit_profile")
	public String showEditProfile(Model model, Principal principal) {
		if (principal != null) {
			Optional<User> user = userRepository.findByEmail(principal.getName());
			if (user.isPresent()) {
				model.addAttribute("user", user.get());
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
			Optional<User> optionalUser = userRepository.findByEmail(principal.getName());

			if (optionalUser.isPresent()) {
				User user = optionalUser.get();
				user.setName(name);

				boolean emailChanged = !user.getEmail().equals(email);
				user.setEmail(email);

				if (password != null && !password.isEmpty()) {
					user.setEncodedPassword(passwordEncoder.encode(password));
				}

				if (image != null && !image.isEmpty()) {
					user.setImage(BlobProxy.generateProxy(image.getInputStream(), image.getSize()));
				}

				userRepository.save(user);

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
    
}
