package es.webapp03.backend.repository;

import es.webapp03.backend.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // Buscar cursos que contengan al menos uno de los tags proporcionados
    @Query("SELECT DISTINCT c FROM Course c JOIN c.tags t WHERE t IN :tags")
    List<Course> findByTags(@Param("tags") List<String> tags);
}


