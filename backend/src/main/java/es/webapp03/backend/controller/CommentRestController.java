package es.webapp03.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import es.webapp03.backend.dto.CommentBasicDTO;
import es.webapp03.backend.dto.CommentDTO;
import es.webapp03.backend.dto.CommentTextDTO;
import es.webapp03.backend.dto.CourseBasicDTO;
import es.webapp03.backend.dto.CourseDTO;
import es.webapp03.backend.model.Comment;
import es.webapp03.backend.model.Course;
import es.webapp03.backend.model.User;
import es.webapp03.backend.repository.CommentRepository;
import es.webapp03.backend.service.CommentService;
import es.webapp03.backend.service.CourseService;
import es.webapp03.backend.dto.CourseMapper;
import es.webapp03.backend.dto.UserBasicDTO;
import es.webapp03.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses/{courseId}/comments")
public class CommentRestController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<CommentDTO>> getCommentsByCourse(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Comment> comments = commentRepository.findByCourseId(courseId, pageable);

        Page<CommentDTO> dtoPage = comments.map(c -> new CommentDTO(
                c.getId(),
                new CourseBasicDTO(
                        c.getCourse().getId(),
                        c.getCourse().getTitle(),
                        c.getCourse().getDescription(),
                        c.getCourse().getImage(),
                        c.getCourse().getNumberOfUsers(),
                        c.getCourse().getTags(),
                        null),
                new UserBasicDTO(
                        c.getUser().getId(),
                        c.getUser().getName(),
                        c.getUser().getEmail()),
                c.getCreatedDate(),
                c.getText()));

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<?> getCommentById(
            @PathVariable Long courseId,
            @PathVariable Long commentId) {

        Optional<Comment> commentOpt = commentRepository.findById(commentId)
                .filter(c -> c.getCourse().getId().equals(courseId));

        if (commentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Comment comment = commentOpt.get();
        Map<String, Object> response = new HashMap<>();
        response.put("id", comment.getId());
        response.put("text", comment.getText());
        response.put("createdDate", comment.getCreatedDate());
        response.put("courseId", comment.getCourse().getId());
        response.put("userId", comment.getUser().getId());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/")
    public ResponseEntity<CommentBasicDTO> createComment(
            @PathVariable Long courseId,
            @RequestBody CommentTextDTO commentTextDTO,
            HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        User author = userService.findEntityByEmail(principal.getName());
        CourseDTO courseDTO = courseService.findById(courseId);
        Course course = courseMapper.toDomain(courseDTO);
        Comment newComment = new Comment();
        newComment.setUser(author);
        newComment.setText(commentTextDTO.text());
        newComment.setCreatedDate(LocalDate.now());
        newComment.setCourse(course);

        Comment savedComment = commentRepository.save(newComment);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommentBasicDTO(
                        savedComment.getId(),
                        savedComment.getCreatedDate(),
                        savedComment.getText()));

    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long courseId,
            @PathVariable Long commentId) {
        if (commentService.existsById(commentId)) {
            commentService.deleteById(commentId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<CommentBasicDTO>> getPaginatedComments(
            @PathVariable Long courseId,
            Pageable pageable) {

        Page<Comment> commentsPage = commentRepository.findByCourseId(courseId, pageable);
        Page<CommentBasicDTO> dtos = commentsPage
                .map(c -> new CommentBasicDTO(c.getId(), c.getCreatedDate(), c.getText()));

        return ResponseEntity.ok(dtos);
    }
}