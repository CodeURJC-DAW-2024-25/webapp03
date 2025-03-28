package es.webapp03.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import es.webapp03.backend.dto.CommentBasicDTO;
import es.webapp03.backend.dto.CommentDTO;
import es.webapp03.backend.model.Comment;
import es.webapp03.backend.model.User;
import es.webapp03.backend.repository.CommentRepository;
import es.webapp03.backend.service.CommentService;
import es.webapp03.backend.service.CourseService;
import es.webapp03.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;
import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private UserService userService;

    /* ========== ENDPOINTS BÁSICOS ========== */

    @GetMapping
    public ResponseEntity<List<CommentBasicDTO>> getCommentsByCourse(@PathVariable Long courseId) {
        List<Comment> comments = commentRepository.findByCourseIdOrderByCreatedDateDesc(courseId);
        List<CommentBasicDTO> dtos = comments.stream()
            .map(c -> new CommentBasicDTO(c.getId(), c.getCreatedDate(), c.getText()))
            .toList();
        return ResponseEntity.ok(dtos);
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

    /* ========== ENDPOINTS DE CREACIÓN ========== */

    @PostMapping("/new")
    public ResponseEntity<CommentBasicDTO> createComment(
        @PathVariable Long courseId,
        @RequestBody CommentBasicDTO commentBasicDTO,
        HttpServletRequest request) {
    
        Principal principal = request.getUserPrincipal();

        User author = userService.findEntityByEmail(principal.getName());
        Comment newComment = new Comment();
        newComment.setUser(author);
        newComment.setText(commentBasicDTO.text());
        newComment.setCreatedDate(LocalDate.now());
        
        Comment savedComment = commentRepository.save(newComment);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommentBasicDTO(
                        savedComment.getId(),
                        savedComment.getCreatedDate(),
                        savedComment.getText()
                ));
   
}



    /* ========== ENDPOINTS DE ELIMINACIÓN ========== */
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


    /* ========== PAGINACIÓN ========== */

    @GetMapping("/paginated")
    public ResponseEntity<Page<CommentBasicDTO>> getPaginatedComments(
            @PathVariable Long courseId,
            Pageable pageable) {
        
        Page<Comment> commentsPage = commentRepository.findByCourseId(courseId, pageable);
        Page<CommentBasicDTO> dtos = commentsPage.map(c -> 
            new CommentBasicDTO(c.getId(), c.getCreatedDate(), c.getText()));
            
        return ResponseEntity.ok(dtos);
    }
}