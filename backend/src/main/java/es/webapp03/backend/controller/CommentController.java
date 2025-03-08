package es.webapp03.backend.controller;

import java.net.URI;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import es.webapp03.backend.model.Comment;
import es.webapp03.backend.model.User;
import es.webapp03.backend.model.Course;
import es.webapp03.backend.repository.CommentRepository;
import es.webapp03.backend.repository.CourseRepository;
import es.webapp03.backend.repository.UserRepository;
import es.webapp03.backend.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CommentController {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private CourseRepository courseRepository;

    @Autowired
	private UserRepository userRepository;

	@Autowired
	private CommentService commentService;

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

	@PostMapping("/courses/{courseId}/comment/upload")
    public ResponseEntity<Void> uploadComment(HttpServletRequest request, @PathVariable Long courseId, @RequestParam("newcomment") String newcomment) {
        try {
            Optional<Course> courseOpt = courseRepository.findById(courseId);
            if (courseOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

			Principal principal = request.getUserPrincipal();
            if (principal == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // No autenticado
            }

            String username = principal.getName();
            Optional<User> userOpt = userRepository.findByEmail(username);

            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Usuario no encontrado
            }

            User user = userOpt.get();


            Course course = courseOpt.get();
			LocalDate date = LocalDate.now();

            // Save comment in the database
			Comment comment = new Comment(course, user, newcomment, date);
			commentRepository.save(comment);

            // Redirect to the course page
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/courses/" + courseId));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/courses/{courseId}/comment/{commentId}/delete")
    public ResponseEntity<byte[]> deleteFile(@PathVariable Long courseId, @PathVariable Long commentId) {

        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isPresent()) {
            commentRepository.delete(commentOpt.get());
        }

        // Redirect to the course page
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/courses/" + courseId));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }


}
