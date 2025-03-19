package es.webapp03.backend.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import es.webapp03.backend.model.Course;
import es.webapp03.backend.model.Comment;
import es.webapp03.backend.model.Material;
import es.webapp03.backend.model.User;
import es.webapp03.backend.service.UserService;
import es.webapp03.backend.service.CourseService;
import es.webapp03.backend.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CourseController {

	@Autowired
	private UserService userService;

	@Autowired
	private CourseService courseService;

	@Autowired
	private CommentService commentService;

	@ModelAttribute
	public void addAttributes(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();

		if (principal != null) {
			model.addAttribute("logged", true);
			model.addAttribute("userName", principal.getName());
			User user = userService.findByEmail(principal.getName());
			model.addAttribute("userFormalName", user.getName());
			model.addAttribute("admin", request.isUserInRole("ADMIN"));
		} else {
			model.addAttribute("logged", false);
		}
	}

	@GetMapping("/courses/{id}")
public String showCourse(Model model, @PathVariable long id) {
    Optional<Course> course = courseService.findById(id);
    if (course.isPresent()) {
        Course c = course.get();

        // Verificar si el usuario está en el curso y agregarlo si no lo está
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (!courseService.isUserInCourse(id, username)) {
            User user = userService.findByEmail(username);
            if (user != null) {
                courseService.addUserToCourse(c, user);
                userService.addCourseToUser(user, c);
            }
        }

        // Asegurarse de que la lista de materiales no sea nula
        if (c.getMaterials() == null) {
            c.setMaterials(new ArrayList<>());
        }

        // Obtener solo los 3 primeros materiales
        List<Material> materials = c.getMaterials();
        List<Material> firstThreeMaterials = materials.subList(0, Math.min(materials.size(), 3));

        // Obtener solo los 3 primeros comentarios ordenados por fecha descendente
        List<Comment> comments = commentService.findByCourseIdOrderByCreatedDateDesc(id);
        List<Comment> firstThreeComments = comments.subList(0, Math.min(comments.size(), 3));

        // Agregar atributos al modelo
        model.addAttribute("course", c);
        model.addAttribute("material", firstThreeMaterials);
        model.addAttribute("comments", firstThreeComments);

        return "course";
    } else {
        return "index";
    }
}

	@GetMapping("/removecourse/{id}")
	public String removeCourse(@PathVariable long id) {
		if (courseService.existsById(id)) {
			courseService.deleteById(id);
		}
		return "redirect:/";
	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/courses/filter")
	public String filterCoursesByTags(@RequestParam List<String> tags, Model model) {
		List<Course> filteredCourses = courseService.findByTags(tags);
		model.addAttribute("courses", filteredCourses);
		return "index"; // Returns the index view with filtered courses
	}

	@PostMapping("/newcourse")
	public String newCourseProcess(Model model, @RequestParam String title, @RequestParam String description,
			@RequestParam(required = false) MultipartFile imageField, @RequestParam String tags) throws IOException {

		Course course = new Course();
		course.setTitle(title);
		course.setDescription(description);
		course.setNumberOfUsers(0);

		// Process tags (separated by commas)
		List<String> tagList = Arrays.asList(tags.split(","));
		course.setTags(tagList);

		// Image management
		if (!imageField.isEmpty()) {
			course.setImageFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
			course.setImage(true);
		}

		courseService.save(course); // Save the course in the database

		model.addAttribute("courseId", course.getId());

		return "redirect:/courses/" + course.getId();
	}

	@GetMapping("/editcourse/{id}")
	public String editCourse(Model model, @PathVariable long id) {
		Optional<Course> course = courseService.findById(id);
		if (course.isPresent()) {
			model.addAttribute("course", course.get());
			return "editcourse";
		} else {
			return "redirect:/index";
		}
	}

	@PostMapping("/editcourse")
	public String editCourseProcess(Model model, @RequestParam Long id, @RequestParam String title,
			@RequestParam String description, @RequestParam(required = false) MultipartFile imageField,
			@RequestParam(required = false) boolean removeImage) throws IOException, SQLException {
		Optional<Course> optionalCourse = courseService.findById(id);
		if (optionalCourse.isPresent()) {
			Course course = optionalCourse.get();
			course.setTitle(title);
			course.setDescription(description);

			if (removeImage) {
				course.setImageFile(null);
				course.setImage(false);
			} else if (!imageField.isEmpty()) {
				course.setImageFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
				course.setImage(true);
			}

			courseService.save(course);
			return "redirect:/courses/" + course.getId();
		} else {
			return "redirect:/index";
		}
	}

	@GetMapping("/courses/loadMore")
	public String loadMoreCourses(@RequestParam int page, Model model) {
		int pageSize = 3; // Number of courses per page
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<Course> coursePage = courseService.findAll(pageable);

		model.addAttribute("courses", coursePage.getContent());
		return "fragments/courseList"; // Returns an html fragment with the courses
	}
}
