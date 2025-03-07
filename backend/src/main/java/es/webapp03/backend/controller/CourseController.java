package es.webapp03.backend.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import es.webapp03.backend.model.Course;
import es.webapp03.backend.model.Comment;
import es.webapp03.backend.model.Material;
import es.webapp03.backend.repository.CourseRepository;
import es.webapp03.backend.repository.CommentRepository;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CourseController {

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private CommentRepository commentRepository;

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

	@GetMapping("/courses/{id}")
	public String showCourse(Model model, @PathVariable long id) {
		Optional<Course> course = courseRepository.findById(id);
		if (course.isPresent()) {
			Course c = course.get();
			if (c.getMaterials() == null) {
				c.setMaterials(new ArrayList<>()); // Ensure materials is never null
			}

			List<Material> m = c.getMaterials();

			List<Comment> comments = commentRepository.findByCourseIdOrderByCreatedDateDesc(id);
			model.addAttribute("course", c);
			model.addAttribute("material", m);
			model.addAttribute("comments", comments);
			return "course";
		} else {
			return "index";
		}
	}

	@GetMapping("/removecourse/{id}")
	public String removeCourse(@PathVariable long id) {
		if (courseRepository.existsById(id)) {
			courseRepository.deleteById(id);
		}
		return "redirect:/";
	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/courses/filter")
	public String filterCoursesByTags(@RequestParam List<String> tags, Model model) {
		List<Course> filteredCourses = courseRepository.findByTags(tags);
		model.addAttribute("courses", filteredCourses);
		return "index"; // Devuelve la vista index con los cursos filtrados
	}

	@PostMapping("/newcourse")
	public String newCourseProcess(Model model, @RequestParam String title, @RequestParam String description,
			@RequestParam(required = false) MultipartFile imageField, @RequestParam String tags) throws IOException {

		Course course = new Course();
		course.setTitle(title);
		course.setDescription(description);

		// Procesar los tags (separados por comas)
		List<String> tagList = Arrays.asList(tags.split(","));
		course.setTags(tagList);

		// Manejo de la imagen
		if (!imageField.isEmpty()) {
			course.setImageFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
			course.setImage(true);
		}

		courseRepository.save(course); // Guarda el curso en la base de datos

		model.addAttribute("courseId", course.getId());

		return "redirect:/courses/" + course.getId(); // Redirige a la página del curso
	}

	@GetMapping("/editcourse/{id}")
	public String editCourse(Model model, @PathVariable long id) {
		Optional<Course> course = courseRepository.findById(id);
		if (course.isPresent()) {
			model.addAttribute("course", course.get());
			return "editCourse"; // Devuelve la plantilla de edición
		} else {
			return "redirect:/index"; // Si el curso no existe, redirige al índice
		}
	}

	@PostMapping("/editcourse")
	public String editCourseProcess(Model model, @RequestParam Long id, @RequestParam String title,
			@RequestParam String description, @RequestParam(required = false) MultipartFile imageField,
			@RequestParam(required = false) boolean removeImage) throws IOException, SQLException {
		Optional<Course> optionalCourse = courseRepository.findById(id);
		if (optionalCourse.isPresent()) {
			Course course = optionalCourse.get();
			course.setTitle(title);
			course.setDescription(description);

			// Manejo de la imagen
			if (removeImage) {
				course.setImageFile(null);
				course.setImage(false);
			} else if (!imageField.isEmpty()) {
				course.setImageFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
				course.setImage(true);
			}

			courseRepository.save(course); // Guarda los cambios en la base de datos
			return "redirect:/courses/" + course.getId(); // Redirige a la página del curso
		} else {
			return "redirect:/index"; // Si el curso no existe, redirige al índice
		}
	}
}
