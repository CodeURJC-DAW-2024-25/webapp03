package es.webapp03.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import es.webapp03.backend.model.Material;


public interface MaterialRepository extends JpaRepository<Material, Long>{

    Page<Material> findByCourseId(Long courseId, Pageable pageable);
    
}
