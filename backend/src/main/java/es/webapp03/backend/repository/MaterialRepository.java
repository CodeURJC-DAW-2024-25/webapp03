package es.webapp03.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.webapp03.backend.model.Material;


public interface MaterialRepository extends JpaRepository<Material, Long>{
    
}
