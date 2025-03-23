package es.webapp03.backend.controller;

import java.net.URI;
import java.security.Principal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;
import es.webapp03.backend.dto.CourseDTO;
import es.webapp03.backend.dto.CourseMapper;
import es.webapp03.backend.model.Comment;
import es.webapp03.backend.model.User;
import es.webapp03.backend.model.Course;
import es.webapp03.backend.service.CommentService;
import es.webapp03.backend.service.CourseService;
import es.webapp03.backend.service.UserService;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseMapper courseMapper;

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
    public ResponseEntity<Void> uploadComment(HttpServletRequest request, 
                                            @PathVariable Long courseId, 
                                            @RequestParam("newcomment") String newcomment) {
        try {
        CourseDTO courseDTO = courseService.findById(courseId);
        if (courseDTO == null) { 
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = principal.getName();
        User user = userService.findEntityByEmail(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Course course = courseMapper.toDomain(courseDTO);
        LocalDate date = LocalDate.now();

        Comment comment = new Comment(course, user, newcomment, date);
        commentService.save(comment);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/courses/" + courseId));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}


    @PostMapping("/courses/{courseId}/comment/{commentId}/delete")
    public ResponseEntity<byte[]> deleteFile(@PathVariable Long courseId, @PathVariable Long commentId) {
        commentService.deleteById(commentId);

        // Redirect to the course page
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/courses/" + courseId));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
    @GetMapping("/courses/{courseId}/comments/load")
    public String loadMoreComments(@PathVariable Long courseId, @RequestParam int page, Model model) {
    int pageSize = 3;
    Pageable pageable = PageRequest.of(page, pageSize);
    Page<Comment> commentsPage = commentService.findByCourseId(courseId, pageable);

    model.addAttribute("comments", commentsPage.getContent());
    return "fragments/commentList";
}
}
