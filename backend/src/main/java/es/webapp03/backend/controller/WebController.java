package es.webapp03.backend.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.webapp03.backend.model.Course;
import es.webapp03.backend.model.Material;
import es.webapp03.backend.model.User;

import es.webapp03.backend.repository.CommentRepository;
import es.webapp03.backend.repository.CourseRepository;
import es.webapp03.backend.repository.MaterialRepository;
import es.webapp03.backend.repository.UserRepository;
import es.webapp03.backend.service.UserService;

@Controller
public class WebController {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private MaterialRepository materialRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

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

	@GetMapping("/")
	public String showCourses(Model model) {

		model.addAttribute("courses", courseRepository.findAll());

		return "index";
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping("/loginerror")
	public String loginerror() {
		return "error";
	}

	@GetMapping("/courses/{id}")
	public String showCourse(Model model, @PathVariable long id) {

		Optional<Course> course = courseRepository.findById(id);
		if (course.isPresent()) {
			model.addAttribute("course", course.get());
			return "course";
		} else {
			return "index";
		}
	}

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

	@GetMapping("/courses/{id}/image")
	public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {

		Optional<Course> course = courseRepository.findById(id);
		if (course.isPresent() && course.get().getImageFile() != null) {

			Resource file = new InputStreamResource(course.get().getImageFile().getBinaryStream());

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
					.contentLength(course.get().getImageFile().length()).body(file);

		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/removecourse/{id}")
	public String removeCourse(@PathVariable long id) {
		if (courseRepository.existsById(id)) {
			courseRepository.deleteById(id);
		}
		return "redirect:/";
	}

	@GetMapping("/profile_page")
	public String showProfilePage(Model model, Principal principal) {
		if (principal != null) {
			String email = principal.getName();
			Optional<User> user = userRepository.findByEmail(email);
	
			if (user.isPresent()) {
				model.addAttribute("name", user.get().getName());
				model.addAttribute("email", user.get().getEmail());
			}
		}
		return "profile_page";
	}
	


	@GetMapping("/newcomment")
	public String newComment(Model model) {

		model.addAttribute("comment", commentRepository.findAll());

		return "course";
	}

	@GetMapping("/newcourse")
	public String showNewCourse() {
		return "newcourse";
	}

	@PreAuthorize("hasRole('USER')")
	@PostMapping("/newcourse")
	public String newCourseProcess(Model model, Course course, MultipartFile imageField) throws IOException {

		if (!imageField.isEmpty()) {
			course.setImageFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
			course.setImage(true);
		}

		courseRepository.save(course);

		model.addAttribute("courseId", course.getId());

		return "redirect:/courses/" + course.getId();
	}

	@GetMapping("/editcourse/{id}")
	public String editCourse(Model model, @PathVariable long id) {

		Optional<Course> course = courseRepository.findById(id);
		if (course.isPresent()) {
			model.addAttribute("course", course.get());
			return "createCourse";
		} else {
			return "index";
		}
	}

	@PostMapping("/editcourse")
	public String editCourseProcess(Model model, Course course, boolean removeImage, MultipartFile imageField)
			throws IOException, SQLException {

		updateImage(course, removeImage, imageField);

		courseRepository.save(course);

		model.addAttribute("courseId", course.getId());

		return "redirect:/courses/" + course.getId();
	}

	private void updateImage(Course course, boolean removeImage, MultipartFile imageField)
			throws IOException, SQLException {

		if (!imageField.isEmpty()) {
			course.setImageFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
			course.setImage(true);
		} else {
			if (removeImage) {
				course.setImageFile(null);
				course.setImage(false);
			} else {
				// Maintain the same image loading it before updating the book
				Course dbCourse = courseRepository.findById(course.getId()).orElseThrow();
				if (dbCourse.getImage()) {
					course.setImageFile(BlobProxy.generateProxy(dbCourse.getImageFile().getBinaryStream(),
							dbCourse.getImageFile().length()));
					course.setImage(true);
				}
			}
		}
	}

}
