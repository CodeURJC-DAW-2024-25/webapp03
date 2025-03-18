package es.webapp03.backend.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.webapp03.backend.model.User;
import es.webapp03.backend.model.Course;
import es.webapp03.backend.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user, String roleName) {
        user.setEncodedPassword(passwordEncoder.encode(user.getEncodedPassword()));
        user.getRoles().add(roleName);
        return userRepository.save(user);
    }

    public void addCourseToUser(User user, Course course){
        user.addCourse(course);
        userRepository.save(user);
    }

    public User findByName(String name) {
        return userRepository.findByName(name).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public void save(User newUser) {
        userRepository.save(newUser);
    }

    public List<User> findByRoles(String role) {
        return userRepository.findByRoles(role);
    }

    public boolean existsById(long id) {
        return userRepository.existsById(id);
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}