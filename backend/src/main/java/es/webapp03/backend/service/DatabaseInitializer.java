package es.webapp03.backend.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
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

    private Blob loadResource(String path) {
        try {
            InputStream resourceStream = getClass().getResourceAsStream(path);
            if (resourceStream != null) {
                byte[] resourceBytes = resourceStream.readAllBytes();
                return new SerialBlob(resourceBytes);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostConstruct
    public void init() throws IOException, URISyntaxException, SQLException {
        Blob defaultCourseImage = loadResource("/static/assets/img/portfolio/5.jpg");
        Blob defaultUserImage = loadResource("/static/assets/user_image_default.jpg");
    
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
        
        Blob image_calculo = loadResource("/static/assets/img/coursesImages/Portada_Calculo.PNG");
        Blob image_DAA = loadResource("/static/assets/img/coursesImages/Portada_DAA.PNG");
        Blob image_DAW = loadResource("/static/assets/img/coursesImages/Portada_DAW.PNG");
        Blob image_EAS = loadResource("/static/assets/img/coursesImages/Portada_EAS.PNG");
        Blob image_ED = loadResource("/static/assets/img/coursesImages/Portada_ED.PNG");
        Blob image_IO = loadResource("/static/assets/img/coursesImages/Portada_IO.PNG");
        Blob image_IP = loadResource("/static/assets/img/coursesImages/Portada_IP.PNG");
        Blob image_MDA = loadResource("/static/assets/img/coursesImages/Portada_MDA.PNG");
        Blob image_POO = loadResource("/static/assets/img/coursesImages/Portada_POO.PNG");
        Blob image_SI = loadResource("/static/assets/img/coursesImages/Portada_SI.PNG");

        Course course_calculo = new Course("Cálculo", "Curso de cálculo. Común a todas las ingenierías", image_calculo, 0);
        course_calculo.setTags(Arrays.asList("Matematicas", "Basica"));

        Course course_DAA = new Course("Diseño y análisis de algoritmos", "En este curso se aprenderá a utilizar y optimizar algoritmos. Se explicarán los algoritmos de forma teórica y se implementarán con Python", image_DAA, 0);
        course_DAA.setTags(Arrays.asList("Programacion", "Python"));

        Course course_DAW= new Course("Desarrollo de aplicaciones web", "En esta asignatura se aprenderá ha desarrollar aplicaciones web simulando una experiencia lo más parecida posible a un desarrollo de una web en un equipo profesional", image_DAW, 0);
        course_DAW.setTags(Arrays.asList("Programacion", "Web"));

        Course course_EAS = new Course("Evolución y adaptación del software", "En EAS se aprenderán los principios básicos para el correcto desarrollo de software que se pueda evolucionar y crecer en el futuro", image_EAS, 0);
        course_EAS.setTags(Arrays.asList("Arquitectura", "Programacion"));

        Course course_ED = new Course("Estructuras de datos", "En este curso se estudiara de forma teórica y se implementaran en Pascal las principales estructuras de datos", image_ED, 0);
        course_ED.setTags(Arrays.asList("Programacion", "Pascal"));

        Course course_IO = new Course("Investigación Operativa", "En esta asignatura se utilizará Python para resolver problemas de investigación operativa", image_IO, 0);
        course_IO.setTags(Arrays.asList("Programacion", "Python"));

        Course course_IP = new Course("Introducción a la programación", "El estudiante aprenderá los conceptos básicos de programación utilizando el lenguaje Pascal", image_IP, 0);
        course_IP.setTags(Arrays.asList("Programacion", "Pascal"));

        Course course_MDA = new Course("Matemática discreta y álgebra", "En MDA el estudiante aprenderá conceptos básicos de matemáticas y álgebra que le serán de gran utilidad para su desarrollo como ingeniero", image_MDA, 0);
        course_MDA.setTags(Arrays.asList("Matematicas", "Basica"));

        Course course_POO = new Course("Programación orientada a objetos", "Se enseñarán los principios de la programación orientada a objetos, para ello se aprenderá a utilizar UML y Java", image_POO, 0);
        course_POO.setTags(Arrays.asList("Programacion", "Java"));

        Course course_SI = new Course("Seguridad informática", "Se aprenderán conceptos básicos de seguridad", image_SI, 0);
        course_SI.setTags(Arrays.asList("Seguridad", "Teorica"));

        courseRepository.saveAll(Arrays.asList(course_calculo, course_DAA, course_DAW, course_EAS, course_ED, course_IO, course_IP, course_MDA, course_POO, course_SI));

        Comment comment1_calculo = new Comment(course_calculo, user1, "¡Excelente curso!", LocalDate.now());
        Comment comment2_calculo = new Comment(course_calculo, user2, "Muy útil, gracias!", LocalDate.now());
        Comment comment3_calculo = new Comment(course_calculo, user3, "Me encantó la dinámica de las clases.", LocalDate.now());
        Comment comment4_calculo = new Comment(course_calculo, user4, "Información clara y bien explicada.", LocalDate.now());
        Comment comment5_calculo = new Comment(course_calculo, user5, "Definitivamente lo recomendaré a mis amigos.", LocalDate.now());
        commentRepository.saveAll(Arrays.asList(comment1_calculo, comment2_calculo, comment3_calculo, comment4_calculo, comment5_calculo));

        Comment comment1_DAA = new Comment(course_DAA, user1, "¡Excelente curso!", LocalDate.now());
        Comment comment2_DAA = new Comment(course_DAA, user2, "Muy útil, gracias!", LocalDate.now());
        Comment comment3_DAA = new Comment(course_DAA, user3, "Me encantó la dinámica de las clases.", LocalDate.now());
        Comment comment4_DAA = new Comment(course_DAA, user4, "Información clara y bien explicada.", LocalDate.now());
        Comment comment5_DAA = new Comment(course_DAA, user5, "Definitivamente lo recomendaré a mis amigos.", LocalDate.now());
        commentRepository.saveAll(Arrays.asList(comment1_DAA, comment2_DAA, comment3_DAA, comment4_DAA, comment5_DAA));

        Comment comment1_DAW = new Comment(course_DAW, user1, "¡Excelente curso!", LocalDate.now());
        Comment comment2_DAW = new Comment(course_DAW, user2, "Muy útil, gracias!", LocalDate.now());
        Comment comment3_DAW = new Comment(course_DAW, user3, "Me encantó la dinámica de las clases.", LocalDate.now());
        Comment comment4_DAW = new Comment(course_DAW, user4, "Información clara y bien explicada.", LocalDate.now());
        Comment comment5_DAW = new Comment(course_DAW, user5, "Definitivamente lo recomendaré a mis amigos.", LocalDate.now());
        commentRepository.saveAll(Arrays.asList(comment1_DAW, comment2_DAW, comment3_DAW, comment4_DAW, comment5_DAW));

        Comment comment1_EAS = new Comment(course_EAS, user1, "¡Excelente curso!", LocalDate.now());
        Comment comment2_EAS = new Comment(course_EAS, user2, "Muy útil, gracias!", LocalDate.now());
        Comment comment3_EAS = new Comment(course_EAS, user3, "Me encantó la dinámica de las clases.", LocalDate.now());
        Comment comment4_EAS = new Comment(course_EAS, user4, "Información clara y bien explicada.", LocalDate.now());
        Comment comment5_EAS = new Comment(course_EAS, user5, "Definitivamente lo recomendaré a mis amigos.", LocalDate.now());
        commentRepository.saveAll(Arrays.asList(comment1_EAS, comment2_EAS, comment3_EAS, comment4_EAS, comment5_EAS));

        Comment comment1_ED = new Comment(course_ED, user1, "¡Excelente curso!", LocalDate.now());
        Comment comment2_ED = new Comment(course_ED, user2, "Muy útil, gracias!", LocalDate.now());
        Comment comment3_ED = new Comment(course_ED, user3, "Me encantó la dinámica de las clases.", LocalDate.now());
        Comment comment4_ED = new Comment(course_ED, user4, "Información clara y bien explicada.", LocalDate.now());
        Comment comment5_ED = new Comment(course_ED, user5, "Definitivamente lo recomendaré a mis amigos.", LocalDate.now());
        commentRepository.saveAll(Arrays.asList(comment1_ED, comment2_ED, comment3_ED, comment4_ED, comment5_ED));

        Comment comment1_IP = new Comment(course_IP, user1, "¡Excelente curso!", LocalDate.now());
        Comment comment2_IP = new Comment(course_IP, user2, "Muy útil, gracias!", LocalDate.now());
        Comment comment3_IP = new Comment(course_IP, user3, "Me encantó la dinámica de las clases.", LocalDate.now());
        Comment comment4_IP = new Comment(course_IP, user4, "Información clara y bien explicada.", LocalDate.now());
        Comment comment5_IP = new Comment(course_IP, user5, "Definitivamente lo recomendaré a mis amigos.", LocalDate.now());
        commentRepository.saveAll(Arrays.asList(comment1_IP, comment2_IP, comment3_IP, comment4_IP, comment5_IP));

        Comment comment1_MDA = new Comment(course_MDA, user1, "¡Excelente curso!", LocalDate.now());
        Comment comment2_MDA = new Comment(course_MDA, user2, "Muy útil, gracias!", LocalDate.now());
        Comment comment3_MDA = new Comment(course_MDA, user3, "Me encantó la dinámica de las clases.", LocalDate.now());
        Comment comment4_MDA = new Comment(course_MDA, user4, "Información clara y bien explicada.", LocalDate.now());
        Comment comment5_MDA = new Comment(course_MDA, user5, "Definitivamente lo recomendaré a mis amigos.", LocalDate.now());
        commentRepository.saveAll(Arrays.asList(comment1_MDA, comment2_MDA, comment3_MDA, comment4_MDA, comment5_MDA));

        Comment comment1_POO = new Comment(course_POO, user1, "¡Excelente curso!", LocalDate.now());
        Comment comment2_POO = new Comment(course_POO, user2, "Muy útil, gracias!", LocalDate.now());
        Comment comment3_POO = new Comment(course_POO, user3, "Me encantó la dinámica de las clases.", LocalDate.now());
        Comment comment4_POO = new Comment(course_POO, user4, "Información clara y bien explicada.", LocalDate.now());
        Comment comment5_POO = new Comment(course_POO, user5, "Definitivamente lo recomendaré a mis amigos.", LocalDate.now());
        commentRepository.saveAll(Arrays.asList(comment1_POO, comment2_POO, comment3_POO, comment4_POO, comment5_POO));

        Comment comment1_SI = new Comment(course_SI, user1, "¡Excelente curso!", LocalDate.now());
        Comment comment2_SI = new Comment(course_SI, user2, "Muy útil, gracias!", LocalDate.now());
        Comment comment3_SI = new Comment(course_SI, user3, "Me encantó la dinámica de las clases.", LocalDate.now());
        Comment comment4_SI = new Comment(course_SI, user4, "Información clara y bien explicada.", LocalDate.now());
        Comment comment5_SI = new Comment(course_SI, user5, "Definitivamente lo recomendaré a mis amigos.", LocalDate.now());
        commentRepository.saveAll(Arrays.asList(comment1_SI, comment2_SI, comment3_SI, comment4_SI, comment5_SI));


        Blob material1Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 1.pdf");
        Blob material2Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 2.pdf");
        Blob material3Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 3.pdf");
        Blob material4Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 4.pdf");
        Blob material5Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 5.pdf");
        Blob material6Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 6.pdf");
        Blob material7Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 7.pdf");
        Blob material8Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 8.pdf");
        Blob material9Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 9.pdf");
        Blob material10Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 10.pdf");
    
        Material mat1_calculo = new Material("Tema 1.pdf", "application/pdf", material1Blob_calculo, course_calculo);
        Material mat2_calculo = new Material("Tema 2.pdf", "application/pdf", material2Blob_calculo, course_calculo);
        Material mat3_calculo = new Material("Tema 3.pdf", "application/pdf", material3Blob_calculo, course_calculo);
        Material mat4_calculo = new Material("Tema 4.pdf", "application/pdf", material4Blob_calculo, course_calculo);
        Material mat5_calculo = new Material("Tema 5.pdf", "application/pdf", material5Blob_calculo, course_calculo);
        Material mat6_calculo = new Material("Tema 6.pdf", "application/pdf", material6Blob_calculo, course_calculo);
        Material mat7_calculo = new Material("Tema 7.pdf", "application/pdf", material7Blob_calculo, course_calculo);
        Material mat8_calculo = new Material("Tema 8.pdf", "application/pdf", material8Blob_calculo, course_calculo);
        Material mat9_calculo = new Material("Tema 9.pdf", "application/pdf", material9Blob_calculo, course_calculo);
        Material mat10_calculo = new Material("Tema 10.pdf", "application/pdf", material10Blob_calculo, course_calculo);

        materialRepository.saveAll(Arrays.asList(mat1_calculo, mat2_calculo, mat3_calculo, mat4_calculo, mat5_calculo, mat6_calculo, mat7_calculo, mat8_calculo, mat9_calculo, mat10_calculo));

        Blob material1Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 1.pdf");
        Blob material2Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 2.pdf");
        Blob material3Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 3.pdf");
        Blob material4Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 4.pdf");
        Blob material5Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 5.pdf");
        Blob material6Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 6.pdf");
        Blob material7Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 7.pdf");
        Blob material8Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 8.pdf");
        Blob material9Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 9.pdf");
        Blob material10Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 10.pdf");
    
        Material mat1_calculo = new Material("Tema 1.pdf", "application/pdf", material1Blob_calculo, course_calculo);
        Material mat2_calculo = new Material("Tema 2.pdf", "application/pdf", material2Blob_calculo, course_calculo);
        Material mat3_calculo = new Material("Tema 3.pdf", "application/pdf", material3Blob_calculo, course_calculo);
        Material mat4_calculo = new Material("Tema 4.pdf", "application/pdf", material4Blob_calculo, course_calculo);
        Material mat5_calculo = new Material("Tema 5.pdf", "application/pdf", material5Blob_calculo, course_calculo);
        Material mat6_calculo = new Material("Tema 6.pdf", "application/pdf", material6Blob_calculo, course_calculo);
        Material mat7_calculo = new Material("Tema 7.pdf", "application/pdf", material7Blob_calculo, course_calculo);
        Material mat8_calculo = new Material("Tema 8.pdf", "application/pdf", material8Blob_calculo, course_calculo);
        Material mat9_calculo = new Material("Tema 9.pdf", "application/pdf", material9Blob_calculo, course_calculo);
        Material mat10_calculo = new Material("Tema 10.pdf", "application/pdf", material10Blob_calculo, course_calculo);

        materialRepository.saveAll(Arrays.asList(mat1_calculo, mat2_calculo, mat3_calculo, mat4_calculo, mat5_calculo, mat6_calculo, mat7_calculo, mat8_calculo, mat9_calculo, mat10_calculo));

        Blob material1Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 1.pdf");
        Blob material2Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 2.pdf");
        Blob material3Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 3.pdf");
        Blob material4Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 4.pdf");
        Blob material5Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 5.pdf");
        Blob material6Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 6.pdf");
        Blob material7Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 7.pdf");
        Blob material8Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 8.pdf");
        Blob material9Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 9.pdf");
        Blob material10Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 10.pdf");
    
        Material mat1_calculo = new Material("Tema 1.pdf", "application/pdf", material1Blob_calculo, course_calculo);
        Material mat2_calculo = new Material("Tema 2.pdf", "application/pdf", material2Blob_calculo, course_calculo);
        Material mat3_calculo = new Material("Tema 3.pdf", "application/pdf", material3Blob_calculo, course_calculo);
        Material mat4_calculo = new Material("Tema 4.pdf", "application/pdf", material4Blob_calculo, course_calculo);
        Material mat5_calculo = new Material("Tema 5.pdf", "application/pdf", material5Blob_calculo, course_calculo);
        Material mat6_calculo = new Material("Tema 6.pdf", "application/pdf", material6Blob_calculo, course_calculo);
        Material mat7_calculo = new Material("Tema 7.pdf", "application/pdf", material7Blob_calculo, course_calculo);
        Material mat8_calculo = new Material("Tema 8.pdf", "application/pdf", material8Blob_calculo, course_calculo);
        Material mat9_calculo = new Material("Tema 9.pdf", "application/pdf", material9Blob_calculo, course_calculo);
        Material mat10_calculo = new Material("Tema 10.pdf", "application/pdf", material10Blob_calculo, course_calculo);

        materialRepository.saveAll(Arrays.asList(mat1_calculo, mat2_calculo, mat3_calculo, mat4_calculo, mat5_calculo, mat6_calculo, mat7_calculo, mat8_calculo, mat9_calculo, mat10_calculo));

        Blob material1Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 1.pdf");
        Blob material2Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 2.pdf");
        Blob material3Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 3.pdf");
        Blob material4Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 4.pdf");
        Blob material5Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 5.pdf");
        Blob material6Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 6.pdf");
        Blob material7Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 7.pdf");
        Blob material8Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 8.pdf");
        Blob material9Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 9.pdf");
        Blob material10Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 10.pdf");
    
        Material mat1_calculo = new Material("Tema 1.pdf", "application/pdf", material1Blob_calculo, course_calculo);
        Material mat2_calculo = new Material("Tema 2.pdf", "application/pdf", material2Blob_calculo, course_calculo);
        Material mat3_calculo = new Material("Tema 3.pdf", "application/pdf", material3Blob_calculo, course_calculo);
        Material mat4_calculo = new Material("Tema 4.pdf", "application/pdf", material4Blob_calculo, course_calculo);
        Material mat5_calculo = new Material("Tema 5.pdf", "application/pdf", material5Blob_calculo, course_calculo);
        Material mat6_calculo = new Material("Tema 6.pdf", "application/pdf", material6Blob_calculo, course_calculo);
        Material mat7_calculo = new Material("Tema 7.pdf", "application/pdf", material7Blob_calculo, course_calculo);
        Material mat8_calculo = new Material("Tema 8.pdf", "application/pdf", material8Blob_calculo, course_calculo);
        Material mat9_calculo = new Material("Tema 9.pdf", "application/pdf", material9Blob_calculo, course_calculo);
        Material mat10_calculo = new Material("Tema 10.pdf", "application/pdf", material10Blob_calculo, course_calculo);

        materialRepository.saveAll(Arrays.asList(mat1_calculo, mat2_calculo, mat3_calculo, mat4_calculo, mat5_calculo, mat6_calculo, mat7_calculo, mat8_calculo, mat9_calculo, mat10_calculo));

        Blob material1Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 1.pdf");
        Blob material2Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 2.pdf");
        Blob material3Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 3.pdf");
        Blob material4Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 4.pdf");
        Blob material5Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 5.pdf");
        Blob material6Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 6.pdf");
        Blob material7Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 7.pdf");
        Blob material8Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 8.pdf");
        Blob material9Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 9.pdf");
        Blob material10Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 10.pdf");
    
        Material mat1_calculo = new Material("Tema 1.pdf", "application/pdf", material1Blob_calculo, course_calculo);
        Material mat2_calculo = new Material("Tema 2.pdf", "application/pdf", material2Blob_calculo, course_calculo);
        Material mat3_calculo = new Material("Tema 3.pdf", "application/pdf", material3Blob_calculo, course_calculo);
        Material mat4_calculo = new Material("Tema 4.pdf", "application/pdf", material4Blob_calculo, course_calculo);
        Material mat5_calculo = new Material("Tema 5.pdf", "application/pdf", material5Blob_calculo, course_calculo);
        Material mat6_calculo = new Material("Tema 6.pdf", "application/pdf", material6Blob_calculo, course_calculo);
        Material mat7_calculo = new Material("Tema 7.pdf", "application/pdf", material7Blob_calculo, course_calculo);
        Material mat8_calculo = new Material("Tema 8.pdf", "application/pdf", material8Blob_calculo, course_calculo);
        Material mat9_calculo = new Material("Tema 9.pdf", "application/pdf", material9Blob_calculo, course_calculo);
        Material mat10_calculo = new Material("Tema 10.pdf", "application/pdf", material10Blob_calculo, course_calculo);

        materialRepository.saveAll(Arrays.asList(mat1_calculo, mat2_calculo, mat3_calculo, mat4_calculo, mat5_calculo, mat6_calculo, mat7_calculo, mat8_calculo, mat9_calculo, mat10_calculo));

        Blob material1Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 1.pdf");
        Blob material2Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 2.pdf");
        Blob material3Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 3.pdf");
        Blob material4Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 4.pdf");
        Blob material5Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 5.pdf");
        Blob material6Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 6.pdf");
        Blob material7Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 7.pdf");
        Blob material8Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 8.pdf");
        Blob material9Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 9.pdf");
        Blob material10Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 10.pdf");
    
        Material mat1_calculo = new Material("Tema 1.pdf", "application/pdf", material1Blob_calculo, course_calculo);
        Material mat2_calculo = new Material("Tema 2.pdf", "application/pdf", material2Blob_calculo, course_calculo);
        Material mat3_calculo = new Material("Tema 3.pdf", "application/pdf", material3Blob_calculo, course_calculo);
        Material mat4_calculo = new Material("Tema 4.pdf", "application/pdf", material4Blob_calculo, course_calculo);
        Material mat5_calculo = new Material("Tema 5.pdf", "application/pdf", material5Blob_calculo, course_calculo);
        Material mat6_calculo = new Material("Tema 6.pdf", "application/pdf", material6Blob_calculo, course_calculo);
        Material mat7_calculo = new Material("Tema 7.pdf", "application/pdf", material7Blob_calculo, course_calculo);
        Material mat8_calculo = new Material("Tema 8.pdf", "application/pdf", material8Blob_calculo, course_calculo);
        Material mat9_calculo = new Material("Tema 9.pdf", "application/pdf", material9Blob_calculo, course_calculo);
        Material mat10_calculo = new Material("Tema 10.pdf", "application/pdf", material10Blob_calculo, course_calculo);

        materialRepository.saveAll(Arrays.asList(mat1_calculo, mat2_calculo, mat3_calculo, mat4_calculo, mat5_calculo, mat6_calculo, mat7_calculo, mat8_calculo, mat9_calculo, mat10_calculo));

        Blob material1Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 1.pdf");
        Blob material2Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 2.pdf");
        Blob material3Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 3.pdf");
        Blob material4Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 4.pdf");
        Blob material5Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 5.pdf");
        Blob material6Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 6.pdf");
        Blob material7Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 7.pdf");
        Blob material8Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 8.pdf");
        Blob material9Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 9.pdf");
        Blob material10Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 10.pdf");
    
        Material mat1_calculo = new Material("Tema 1.pdf", "application/pdf", material1Blob_calculo, course_calculo);
        Material mat2_calculo = new Material("Tema 2.pdf", "application/pdf", material2Blob_calculo, course_calculo);
        Material mat3_calculo = new Material("Tema 3.pdf", "application/pdf", material3Blob_calculo, course_calculo);
        Material mat4_calculo = new Material("Tema 4.pdf", "application/pdf", material4Blob_calculo, course_calculo);
        Material mat5_calculo = new Material("Tema 5.pdf", "application/pdf", material5Blob_calculo, course_calculo);
        Material mat6_calculo = new Material("Tema 6.pdf", "application/pdf", material6Blob_calculo, course_calculo);
        Material mat7_calculo = new Material("Tema 7.pdf", "application/pdf", material7Blob_calculo, course_calculo);
        Material mat8_calculo = new Material("Tema 8.pdf", "application/pdf", material8Blob_calculo, course_calculo);
        Material mat9_calculo = new Material("Tema 9.pdf", "application/pdf", material9Blob_calculo, course_calculo);
        Material mat10_calculo = new Material("Tema 10.pdf", "application/pdf", material10Blob_calculo, course_calculo);

        materialRepository.saveAll(Arrays.asList(mat1_calculo, mat2_calculo, mat3_calculo, mat4_calculo, mat5_calculo, mat6_calculo, mat7_calculo, mat8_calculo, mat9_calculo, mat10_calculo));

        Blob material1Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 1.pdf");
        Blob material2Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 2.pdf");
        Blob material3Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 3.pdf");
        Blob material4Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 4.pdf");
        Blob material5Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 5.pdf");
        Blob material6Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 6.pdf");
        Blob material7Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 7.pdf");
        Blob material8Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 8.pdf");
        Blob material9Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 9.pdf");
        Blob material10Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 10.pdf");
    
        Material mat1_calculo = new Material("Tema 1.pdf", "application/pdf", material1Blob_calculo, course_calculo);
        Material mat2_calculo = new Material("Tema 2.pdf", "application/pdf", material2Blob_calculo, course_calculo);
        Material mat3_calculo = new Material("Tema 3.pdf", "application/pdf", material3Blob_calculo, course_calculo);
        Material mat4_calculo = new Material("Tema 4.pdf", "application/pdf", material4Blob_calculo, course_calculo);
        Material mat5_calculo = new Material("Tema 5.pdf", "application/pdf", material5Blob_calculo, course_calculo);
        Material mat6_calculo = new Material("Tema 6.pdf", "application/pdf", material6Blob_calculo, course_calculo);
        Material mat7_calculo = new Material("Tema 7.pdf", "application/pdf", material7Blob_calculo, course_calculo);
        Material mat8_calculo = new Material("Tema 8.pdf", "application/pdf", material8Blob_calculo, course_calculo);
        Material mat9_calculo = new Material("Tema 9.pdf", "application/pdf", material9Blob_calculo, course_calculo);
        Material mat10_calculo = new Material("Tema 10.pdf", "application/pdf", material10Blob_calculo, course_calculo);

        materialRepository.saveAll(Arrays.asList(mat1_calculo, mat2_calculo, mat3_calculo, mat4_calculo, mat5_calculo, mat6_calculo, mat7_calculo, mat8_calculo, mat9_calculo, mat10_calculo));

        Blob material1Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 1.pdf");
        Blob material2Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 2.pdf");
        Blob material3Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 3.pdf");
        Blob material4Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 4.pdf");
        Blob material5Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 5.pdf");
        Blob material6Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 6.pdf");
        Blob material7Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 7.pdf");
        Blob material8Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 8.pdf");
        Blob material9Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 9.pdf");
        Blob material10Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 10.pdf");
    
        Material mat1_calculo = new Material("Tema 1.pdf", "application/pdf", material1Blob_calculo, course_calculo);
        Material mat2_calculo = new Material("Tema 2.pdf", "application/pdf", material2Blob_calculo, course_calculo);
        Material mat3_calculo = new Material("Tema 3.pdf", "application/pdf", material3Blob_calculo, course_calculo);
        Material mat4_calculo = new Material("Tema 4.pdf", "application/pdf", material4Blob_calculo, course_calculo);
        Material mat5_calculo = new Material("Tema 5.pdf", "application/pdf", material5Blob_calculo, course_calculo);
        Material mat6_calculo = new Material("Tema 6.pdf", "application/pdf", material6Blob_calculo, course_calculo);
        Material mat7_calculo = new Material("Tema 7.pdf", "application/pdf", material7Blob_calculo, course_calculo);
        Material mat8_calculo = new Material("Tema 8.pdf", "application/pdf", material8Blob_calculo, course_calculo);
        Material mat9_calculo = new Material("Tema 9.pdf", "application/pdf", material9Blob_calculo, course_calculo);
        Material mat10_calculo = new Material("Tema 10.pdf", "application/pdf", material10Blob_calculo, course_calculo);

        materialRepository.saveAll(Arrays.asList(mat1_calculo, mat2_calculo, mat3_calculo, mat4_calculo, mat5_calculo, mat6_calculo, mat7_calculo, mat8_calculo, mat9_calculo, mat10_calculo));

        Blob material1Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 1.pdf");
        Blob material2Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 2.pdf");
        Blob material3Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 3.pdf");
        Blob material4Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 4.pdf");
        Blob material5Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 5.pdf");
        Blob material6Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 6.pdf");
        Blob material7Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 7.pdf");
        Blob material8Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 8.pdf");
        Blob material9Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 9.pdf");
        Blob material10Blob_calculo = loadResource("/static/assets/materials/Calculo/Tema 10.pdf");
    
        Material mat1_calculo = new Material("Tema 1.pdf", "application/pdf", material1Blob_calculo, course_calculo);
        Material mat2_calculo = new Material("Tema 2.pdf", "application/pdf", material2Blob_calculo, course_calculo);
        Material mat3_calculo = new Material("Tema 3.pdf", "application/pdf", material3Blob_calculo, course_calculo);
        Material mat4_calculo = new Material("Tema 4.pdf", "application/pdf", material4Blob_calculo, course_calculo);
        Material mat5_calculo = new Material("Tema 5.pdf", "application/pdf", material5Blob_calculo, course_calculo);
        Material mat6_calculo = new Material("Tema 6.pdf", "application/pdf", material6Blob_calculo, course_calculo);
        Material mat7_calculo = new Material("Tema 7.pdf", "application/pdf", material7Blob_calculo, course_calculo);
        Material mat8_calculo = new Material("Tema 8.pdf", "application/pdf", material8Blob_calculo, course_calculo);
        Material mat9_calculo = new Material("Tema 9.pdf", "application/pdf", material9Blob_calculo, course_calculo);
        Material mat10_calculo = new Material("Tema 10.pdf", "application/pdf", material10Blob_calculo, course_calculo);

        materialRepository.saveAll(Arrays.asList(mat1_calculo, mat2_calculo, mat3_calculo, mat4_calculo, mat5_calculo, mat6_calculo, mat7_calculo, mat8_calculo, mat9_calculo, mat10_calculo));
    }
}
