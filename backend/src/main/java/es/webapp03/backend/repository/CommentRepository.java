package es.webapp03.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.webapp03.backend.model.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    
}
