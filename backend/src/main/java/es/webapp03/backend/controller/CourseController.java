package es.webapp03.backend.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import es.webapp03.backend.repository.CourseRepository;

@Controller
public class CourseController {

    @Autowired
	private CourseRepository courseRepository;

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

	@DeleteMapping("/removecourse/{id}")
	public String removeCourse(@PathVariable long id) {
		if (courseRepository.existsById(id)) {
			courseRepository.deleteById(id);
		}
		return "redirect:/";
	}

	@PreAuthorize("hasRole('USER')")
	@PostMapping("/newcourse")
	public String newCourseProcess(Model model, @RequestParam String title, @RequestParam String description, @RequestParam MultipartFile imageField) throws IOException {

		Course course = new Course();
		course.setTitle(title);
		course.setDescription(description);

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
			return "editCourse"; // Devuelve la plantilla de edición
		} else {
			return "redirect:/index"; // Si el curso no existe, redirige al índice
		}
	}

	@PostMapping("/editcourse")
	public String editCourseProcess(Model model, @RequestParam Long id, @RequestParam String title, @RequestParam String description, @RequestParam(required = false) MultipartFile imageField, @RequestParam(required = false) boolean removeImage) throws IOException, SQLException {
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
