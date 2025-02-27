package es.webapp03.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import es.webapp03.backend.model.User;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);

    List<User> findByRoles(List<String> roles);
}
