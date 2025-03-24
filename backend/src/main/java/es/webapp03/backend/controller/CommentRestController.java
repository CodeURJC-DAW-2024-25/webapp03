package es.webapp03.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import es.webapp03.backend.dto.CommentBasicDTO;
import es.webapp03.backend.dto.CommentDTO;
import es.webapp03.backend.model.Comment;
import es.webapp03.backend.service.CommentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses/{courseId}/comments")
public class CommentRestController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentBasicDTO>> getCommentsByCourse(@PathVariable Long courseId) {
        List<CommentBasicDTO> comments = commentService.findCommentsByCourseId(courseId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(
            @PathVariable Long courseId,
            @PathVariable Long commentId) {
        Optional<CommentDTO> comment = commentService.findById(commentId);
        return comment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommentBasicDTO> createComment(
            @PathVariable Long courseId,
            @RequestParam String text) {
        // Necesitarás implementar este método en el servicio
        CommentBasicDTO savedComment = commentService.createComment(courseId, text);
        return ResponseEntity.ok(savedComment);
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
        Page<Comment> commentsPage = commentService.findByCourseId(courseId, pageable);
        Page<CommentBasicDTO> dtos = commentsPage.map(c -> new CommentBasicDTO(
                c.getId(), 
                c.getCreatedDate(), 
                c.getText()));
        return ResponseEntity.ok(dtos);
    }
}