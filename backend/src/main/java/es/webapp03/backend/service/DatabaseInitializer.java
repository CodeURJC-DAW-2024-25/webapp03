package es.webapp03.backend.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.webapp03.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import es.webapp03.backend.repository.MaterialRepository;
import es.webapp03.backend.repository.CourseRepository;
import es.webapp03.backend.model.User;
import es.webapp03.backend.model.Course;
import es.webapp03.backend.model.Material;
import es.webapp03.backend.model.Commentary;
import es.webapp03.backend.repository.CommentRepository;

@Service
public class DatabaseInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() throws IOException, URISyntaxException {

        userRepository.save(new User("Laura", "laura@gmail.com", passwordEncoder.encode("laurapass"), null, "USER"));
        userRepository.save(new User("Domingo", "domingo@gmail.com", passwordEncoder.encode("domingopass"), null, "USER"));
        userRepository.save(new User("Marcos", "marcos@gmail.com", passwordEncoder.encode("marcospass"), null, "USER"));
        userRepository.save(new User("Juan", "juan@gmail.com", passwordEncoder.encode("juanpass"), null, "USER"));
        userRepository.save(new User("Sergio", "sergio@gmail.com", passwordEncoder.encode("sergiopass"), null, "USER"));
        userRepository.save(new User("David", "david@gmail.com", passwordEncoder.encode("davidpass"), null, "USER"));
        userRepository.save(new User("Paula", "paula@gmail.com", passwordEncoder.encode("paulapass"), null, "USER"));
        userRepository.save(new User("Gonzalo", "gonzalo@gmail.com", passwordEncoder.encode("gonzalopass"), null, "USER"));
        userRepository.save(new User("Marta", "marta@gmail.com", passwordEncoder.encode("martapass"), null, "USER"));
        userRepository.save(new User("Sofía", "sofia@gmail.com", passwordEncoder.encode("sofiapass"), null, "USER"));
        userRepository.save(new User("María", "maria@gmail.com", passwordEncoder.encode("mariapass"), null, "USER"));
        userRepository.save(new User("Raúl", "raul@gmail.com", passwordEncoder.encode("raulpass"), null, "USER"));
        userRepository.save(new User("admin", "admin@gmail.com", passwordEncoder.encode("adminpass"), null, "USER", "ADMIN"));

        Course course1 = new Course("Course 1", "Description 1", null);
        course1.setTags(Arrays.asList("Tag1", "Tag2", "Tag3"));

        Course course2 = new Course("Course 2", "Description 2", null);
        course2.setTags(Arrays.asList("Tag2", "Tag4"));

        Course course3 = new Course("Course 3", "Description 3", null);
        course3.setTags(Arrays.asList("Tag1", "Tag5"));

        Course course4 = new Course("Course 4", "Description 4", null);
        course4.setTags(Arrays.asList("Tag3", "Tag6"));

        Course course5 = new Course("Course 5", "Description 5", null);
        course5.setTags(Arrays.asList("Tag4", "Tag7"));

        Course course6 = new Course("Course 6", "Description 6", null);
        course6.setTags(Arrays.asList("Tag5", "Tag8"));

        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);
        courseRepository.save(course4);
        courseRepository.save(course5);
        courseRepository.save(course6);
    }
}
