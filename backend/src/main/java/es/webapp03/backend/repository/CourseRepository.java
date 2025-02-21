package es.webapp03.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.webapp03.backend.model.Course;


public interface CourseRepository extends JpaRepository<Course, Long>{
    
}
