package es.webapp03.backend.repository;

import es.webapp03.backend.model.Course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @SuppressWarnings("null")
    Page<Course> findAll(Pageable pageable);

    // Search for courses that contain at least one of the provided tags
    @Query("SELECT DISTINCT c FROM Course c JOIN c.tags t WHERE t IN :tags")
    List<Course> findByTags(@Param("tags") List<String> tags);
    
    Page<Course> findByTags(@Param("tags") List<String> tags, Pageable pageable);

    List<Course> findTop3ByOrderByNumberOfUsersDesc();
}


