package es.webapp03.backend.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.webapp03.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import es.webapp03.backend.repository.MaterialRepository;
import es.webapp03.backend.repository.CourseRepository;
import es.webapp03.backend.model.User;
import es.webapp03.backend.model.Comment;
import es.webapp03.backend.model.Course;
import es.webapp03.backend.model.Material;
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

    private Blob loadDefaultImage() {
        try {
            InputStream defaultImageStream = getClass().getResourceAsStream("/static/assets/img/portfolio/5.jpg");
            if (defaultImageStream != null) {
                byte[] imageBytes = defaultImageStream.readAllBytes();
                return new SerialBlob(imageBytes);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Blob loadDefaultUserImage() {
        try {
            InputStream defaultUserImageStream = getClass()
                    .getResourceAsStream("/static/assets/user_image_default.jpg");
            if (defaultUserImageStream != null) {
                byte[] imageBytes = defaultUserImageStream.readAllBytes();
                return new SerialBlob(imageBytes);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Blob loadDefaultMaterial(String path) {
        try {
            InputStream materialStream = getClass().getResourceAsStream(path);
            if (materialStream != null) {
                byte[] materialBytes = materialStream.readAllBytes();
                return new SerialBlob(materialBytes);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }    
    

    @PostConstruct
    public void init() throws IOException, URISyntaxException, SQLException {
        Blob defaultCourseImage = loadDefaultImage(); // Imagen por defecto para cursos
        Blob defaultUserImage = loadDefaultUserImage(); // Imagen por defecto para usuarios
    
        userRepository.save(new User("Laura", "laura@gmail.com", passwordEncoder.encode("laurapass"), defaultUserImage, "USER"));
        userRepository.save(new User("Domingo", "domingo@gmail.com", passwordEncoder.encode("domingopass"), defaultUserImage, "USER"));
        userRepository.save(new User("Marcos", "marcos@gmail.com", passwordEncoder.encode("marcospass"), defaultUserImage, "USER"));
        userRepository.save(new User("Juan", "juan@gmail.com", passwordEncoder.encode("juanpass"), defaultUserImage, "USER"));
        userRepository.save(new User("Sergio", "sergio@gmail.com", passwordEncoder.encode("sergiopass"), defaultUserImage, "USER"));
        userRepository.save(new User("David", "david@gmail.com", passwordEncoder.encode("davidpass"), defaultUserImage, "USER"));
        userRepository.save(new User("Paula", "paula@gmail.com", passwordEncoder.encode("paulapass"), defaultUserImage, "USER"));
        userRepository.save(new User("Gonzalo", "gonzalo@gmail.com", passwordEncoder.encode("gonzalopass"), defaultUserImage, "USER"));
        userRepository.save(new User("Marta", "marta@gmail.com", passwordEncoder.encode("martapass"), defaultUserImage, "USER"));
        userRepository.save(new User("Sofía", "sofia@gmail.com", passwordEncoder.encode("sofiapass"), defaultUserImage, "USER"));
        userRepository.save(new User("María", "maria@gmail.com", passwordEncoder.encode("mariapass"), defaultUserImage, "USER"));
        userRepository.save(new User("Raúl", "raul@gmail.com", passwordEncoder.encode("raulpass"), defaultUserImage, "USER"));
        userRepository.save(new User("admin", "admin@gmail.com", passwordEncoder.encode("adminpass"), defaultUserImage, "ADMIN"));
    
        User user1 = userRepository.findByEmail("laura@gmail.com").orElseThrow();
        User user2 = userRepository.findByEmail("domingo@gmail.com").orElseThrow();
        User user3 = userRepository.findByEmail("marcos@gmail.com").orElseThrow();
        User user4 = userRepository.findByEmail("juan@gmail.com").orElseThrow();
        User user5 = userRepository.findByEmail("sergio@gmail.com").orElseThrow();
        User user6 = userRepository.findByEmail("david@gmail.com").orElseThrow();
        User user7 = userRepository.findByEmail("paula@gmail.com").orElseThrow();
        User user8 = userRepository.findByEmail("gonzalo@gmail.com").orElseThrow();
        User user9 = userRepository.findByEmail("marta@gmail.com").orElseThrow();
        User user10 = userRepository.findByEmail("sofia@gmail.com").orElseThrow();


        Course course1 = new Course("Course 1", "Description 1", defaultCourseImage);
        course1.setTags(Arrays.asList("Tag1", "Tag2", "Tag3"));

        Course course2 = new Course("Course 2", "Description 2", defaultCourseImage);
        course2.setTags(Arrays.asList("Tag2", "Tag4"));

        Course course3 = new Course("Course 3", "Description 3", defaultCourseImage);
        course3.setTags(Arrays.asList("Tag1", "Tag5"));

        Course course4 = new Course("Course 4", "Description 4", defaultCourseImage);
        course4.setTags(Arrays.asList("Tag3", "Tag6"));

        Course course5 = new Course("Course 5", "Description 5", defaultCourseImage);
        course5.setTags(Arrays.asList("Tag4", "Tag7"));

        Course course6 = new Course("Course 6", "Description 6", defaultCourseImage);
        course6.setTags(Arrays.asList("Tag5", "Tag8"));

        courseRepository.saveAll(Arrays.asList(course1, course2, course3, course4, course5, course6));

        Comment comment1 = new Comment(course1, user1, "¡Excelente curso!", LocalDate.now());
        Comment comment2 = new Comment(course1, user2, "Muy útil, gracias!", LocalDate.now());
        Comment comment3 = new Comment(course1, user3, "Me encantó la dinámica de las clases.", LocalDate.now());
        Comment comment4 = new Comment(course1, user4, "Información clara y bien explicada.", LocalDate.now());
        Comment comment5 = new Comment(course1, user5, "Definitivamente lo recomendaré a mis amigos.", LocalDate.now());
        Comment comment6 = new Comment(course1, user6, "El contenido es justo lo que necesitaba.", LocalDate.now());
        Comment comment7 = new Comment(course1, user7, "Podría haber más ejemplos prácticos, pero en general muy bueno.", LocalDate.now());
        Comment comment8 = new Comment(course1, user8, "Aprendí mucho en poco tiempo, excelente curso.", LocalDate.now());
        Comment comment9 = new Comment(course1, user9, "El profesor explica de manera muy sencilla.", LocalDate.now());
        Comment comment10 = new Comment(course1, user10, "Me gustaría que agregaran más ejercicios.", LocalDate.now());
        commentRepository.saveAll(Arrays.asList(comment1, comment2, comment3, comment4, comment5, comment6, comment7, comment8, comment9, comment10));
        
        Blob material1Blob = loadDefaultMaterial("/static/assets/materials/Tema3.1-SeguridadWeb.pdf");
        Blob material2Blob = loadDefaultMaterial("/static/assets/materials/Fundamentos.pdf");
        Blob material3Blob = loadDefaultMaterial("/static/assets/materials/practica1_gic_gis.pdf");
    
        Material mat1 = new Material("Tema3.1-SeguridadWeb.pdf", "application/pdf", material1Blob, course1);
        Material mat2 = new Material("Fundamentos.pdf", "application/pdf", material2Blob, course1);
        Material mat3 = new Material("practica1_gic_gis.pdf", "application/pdf", material3Blob, course1);

        materialRepository.saveAll(Arrays.asList(mat1, mat2, mat3));
        
    }
}
