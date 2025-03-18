package es.webapp03.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import es.webapp03.backend.model.Comment;



public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByCourseIdOrderByCreatedDateDesc(Long courseId);

    Page<Comment> findByCourseId(Long courseId, Pageable pageable);
    
}
