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
    private CourseService courseService;

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

        Comment comment1_calculo = new Comment(course_calculo, user1, "El curso me ayudó a comprender mejor los conceptos de derivadas e integrales. Las explicaciones son claras y los ejemplos bien elegidos.", LocalDate.now());
        Comment comment2_calculo = new Comment(course_calculo, user2, "Algunas partes avanzadas son algo rápidas, pero los ejercicios prácticos ayudan mucho a reforzar lo aprendido.", LocalDate.now());
        Comment comment3_calculo = new Comment(course_calculo, user3, "Me gustó la forma en que se explican los límites, nunca los había entendido tan bien.", LocalDate.now());
        Comment comment4_calculo = new Comment(course_calculo, user4, "Buen curso, aunque me hubiera gustado que se incluyeran más problemas resueltos paso a paso.", LocalDate.now());
        Comment comment5_calculo = new Comment(course_calculo, user5, "Si tienes problemas con cálculo, este curso es muy recomendable. Explica los fundamentos de manera clara y sin rodeos.", LocalDate.now());
        commentRepository.saveAll(Arrays.asList(comment1_calculo, comment2_calculo, comment3_calculo, comment4_calculo, comment5_calculo));

        Comment comment1_DAA = new Comment(course_DAA, user1, "Excelente curso para comprender el diseño de algoritmos. Me ayudó a mejorar mi forma de pensar en la optimización.", LocalDate.now());
        Comment comment2_DAA = new Comment(course_DAA, user3, "El profesor explica muy bien, pero algunos temas requieren conocimiento previo para seguir el ritmo.", LocalDate.now());
        Comment comment3_DAA = new Comment(course_DAA, user5, "Me gustó mucho la parte de backtracking y programación dinámica. Muy bien explicados los ejemplos.", LocalDate.now());
        Comment comment4_DAA = new Comment(course_DAA, user7, "El curso está bien estructurado, aunque faltaron más problemas complejos para practicar.", LocalDate.now());
        Comment comment5_DAA = new Comment(course_DAA, user9, "Si buscas aprender sobre eficiencia algorítmica, este curso es una muy buena opción.", LocalDate.now());
        commentRepository.saveAll(Arrays.asList(comment1_DAA, comment2_DAA, comment3_DAA, comment4_DAA, comment5_DAA));

        Comment comment1_DAW = new Comment(course_DAW, user2, "Muy completo en cuanto a HTML, CSS y JavaScript, pero podría profundizar más en frameworks modernos.", LocalDate.now());
        Comment comment2_DAW = new Comment(course_DAW, user4, "Me gustó mucho la parte de diseño responsivo, aprendí a hacer páginas bien estructuradas.", LocalDate.now());
        Comment comment3_DAW = new Comment(course_DAW, user6, "Las explicaciones son claras, pero me hubiera gustado más proyectos prácticos.", LocalDate.now());
        Comment comment4_DAW = new Comment(course_DAW, user8, "Es un buen curso si estás empezando en desarrollo web, pero si ya tienes conocimientos básicos, se queda algo corto.", LocalDate.now());
        Comment comment5_DAW = new Comment(course_DAW, user10, "Gracias a este curso, logré desarrollar mi primera página web completa.", LocalDate.now());
        commentRepository.saveAll(Arrays.asList(comment1_DAW, comment2_DAW, comment3_DAW, comment4_DAW, comment5_DAW));

        Comment comment1_EAS = new Comment(course_EAS, user6, "El contenido de patrones de diseño me pareció excelente. Me ayudó mucho en mi proyecto de software.", LocalDate.now());
        Comment comment2_EAS = new Comment(course_EAS, user7, "Muy bien explicado, aunque algunos conceptos requieren conocimientos previos en arquitectura de software.", LocalDate.now());
        Comment comment3_EAS = new Comment(course_EAS, user8, "La parte teórica está bien cubierta, pero me hubiera gustado más ejemplos prácticos.", LocalDate.now());
        Comment comment4_EAS = new Comment(course_EAS, user9, "Me ayudó a entender cómo diseñar software escalable y bien estructurado.", LocalDate.now());
        Comment comment5_EAS = new Comment(course_EAS, user10, "Un curso recomendable si quieres aprender sobre arquitectura de software desde una base sólida.", LocalDate.now());
        commentRepository.saveAll(Arrays.asList(comment1_EAS, comment2_EAS, comment3_EAS, comment4_EAS, comment5_EAS));

        Comment comment1_ED = new Comment(course_ED, user3, "Un excelente repaso de estructuras de datos. Me ayudó a reforzar listas y árboles binarios.", LocalDate.now());
        Comment comment2_ED = new Comment(course_ED, user4, "Me gustó la forma en que explicaron las pilas y colas, con ejemplos prácticos bien pensados.", LocalDate.now());
        Comment comment3_ED = new Comment(course_ED, user5, "Curso recomendable, aunque la parte de grafos fue algo difícil de seguir sin conocimientos previos.", LocalDate.now());
        Comment comment4_ED = new Comment(course_ED, user6, "Me sirvió mucho para entrevistas técnicas, especialmente la parte de listas enlazadas.", LocalDate.now());
        Comment comment5_ED = new Comment(course_ED, user7, "Un buen curso, pero sería ideal incluir más ejercicios complejos para afianzar los conceptos.", LocalDate.now());
        commentRepository.saveAll(Arrays.asList(comment1_ED, comment2_ED, comment3_ED, comment4_ED, comment5_ED));

        Comment comment1_IP = new Comment(course_IP, user1, "Un curso básico pero bien explicado, ideal para quienes inician en la programación.", LocalDate.now());
        Comment comment2_IP = new Comment(course_IP, user4, "Los fundamentos están bien cubiertos, aunque me hubiera gustado más ejercicios prácticos.", LocalDate.now());
        Comment comment3_IP = new Comment(course_IP, user6, "Me ayudó a entender mejor la lógica de programación con ejemplos sencillos.", LocalDate.now());
        Comment comment4_IP = new Comment(course_IP, user7, "Las explicaciones son claras y el ritmo es adecuado para principiantes.", LocalDate.now());
        Comment comment5_IP = new Comment(course_IP, user9, "Si nunca has programado antes, este curso te dará una buena base.", LocalDate.now());
        commentRepository.saveAll(Arrays.asList(comment1_IP, comment2_IP, comment3_IP, comment4_IP, comment5_IP));

        Comment comment1_MDA = new Comment(course_MDA, user8, "El curso explica muy bien el análisis de datos y cómo aplicar modelos matemáticos en problemas reales.", LocalDate.now());
        Comment comment2_MDA = new Comment(course_MDA, user9, "Me gustó la forma en que presentan los algoritmos de optimización, aunque algunos temas avanzados requieren repasar varias veces.", LocalDate.now());
        Comment comment3_MDA = new Comment(course_MDA, user6, "Muy interesante la parte de machine learning, aunque esperaba más ejemplos prácticos con datasets reales.", LocalDate.now());
        Comment comment4_MDA = new Comment(course_MDA, user2, "Explicaciones claras y ejercicios bien estructurados. Me sirvió mucho para entender la teoría detrás del análisis de datos.", LocalDate.now());
        Comment comment5_MDA = new Comment(course_MDA, user1, "Un curso esencial si quieres profundizar en matemáticas aplicadas a la ciencia de datos.", LocalDate.now());
        commentRepository.saveAll(Arrays.asList(comment1_MDA, comment2_MDA, comment3_MDA, comment4_MDA, comment5_MDA));

        Comment comment1_POO = new Comment(course_POO, user3, "Un curso imprescindible para aprender los principios de la programación orientada a objetos. Explicaciones claras y ejemplos bien elegidos.", LocalDate.now());
        Comment comment2_POO = new Comment(course_POO, user6, "Aprendí a aplicar correctamente herencia y polimorfismo. Muy útil para quienes quieren mejorar su código.", LocalDate.now());
        Comment comment3_POO = new Comment(course_POO, user7, "Me encantó la forma en que explican la encapsulación y cómo aplicarla en proyectos reales.", LocalDate.now());
        Comment comment4_POO = new Comment(course_POO, user8, "Los ejercicios son muy buenos, pero algunos temas podrían profundizar más en patrones de diseño.", LocalDate.now());
        Comment comment5_POO = new Comment(course_POO, user9, "Después de este curso, me siento más preparado para estructurar mejor mis programas. Muy recomendado.", LocalDate.now());
        commentRepository.saveAll(Arrays.asList(comment1_POO, comment2_POO, comment3_POO, comment4_POO, comment5_POO));

        Comment comment1_SI = new Comment(course_SI, user2, "Un curso muy completo sobre sistemas de información. Me ayudó a entender la relación entre bases de datos y aplicaciones empresariales.", LocalDate.now());
        Comment comment2_SI = new Comment(course_SI, user3, "Muy útil para comprender cómo gestionar grandes volúmenes de información de manera eficiente.", LocalDate.now());
        Comment comment3_SI = new Comment(course_SI, user5, "Me gustó la parte de análisis de requerimientos y diseño de sistemas, es clave para desarrollar software de calidad.", LocalDate.now());
        Comment comment4_SI = new Comment(course_SI, user8, "El contenido es bueno, pero hubiera sido ideal incluir más casos prácticos de implementación en empresas.", LocalDate.now());
        Comment comment5_SI = new Comment(course_SI, user10, "Definitivamente lo recomendaré a mis compañeros, muy útil para quienes trabajan con bases de datos y sistemas de gestión.", LocalDate.now());
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

        Blob material1Blob_DAA = loadResource("/static/assets/materials/DAA/AlgGrafos.pdf");
        Blob material2Blob_DAA = loadResource("/static/assets/materials/DAA/BT.pdf");
        Blob material3Blob_DAA = loadResource("/static/assets/materials/DAA/DyV.pdf");
        Blob material4Blob_DAA = loadResource("/static/assets/materials/DAA/Voraces genericos.pdf");
        Blob material5Blob_DAA = loadResource("/static/assets/materials/DAA/VoracesEnGrafos.pdf");
        Blob material6Blob_DAA = loadResource("/static/assets/materials/DAA/Tema 6 Ramificación y poda.pdf");
    
        Material mat1_DAA = new Material("AlgGrafos.pdf", "application/pdf", material1Blob_DAA, course_DAA);
        Material mat2_DAA = new Material("BT.pdf", "application/pdf", material2Blob_DAA, course_DAA);
        Material mat3_DAA = new Material("DyV.pdf", "application/pdf", material3Blob_DAA, course_DAA);
        Material mat4_DAA = new Material("Voraces genericos.pdf", "application/pdf", material4Blob_DAA, course_DAA);
        Material mat5_DAA = new Material("VoracesEnGrafos.pdf", "application/pdf", material5Blob_DAA, course_DAA);
        Material mat6_DAA = new Material("Tema 6 Ramificación y poda.pdf", "application/pdf", material6Blob_DAA, course_DAA);

        materialRepository.saveAll(Arrays.asList(mat1_DAA, mat2_DAA, mat3_DAA, mat4_DAA, mat5_DAA, mat6_DAA));

        Blob material1Blob_DAW = loadResource("/static/assets/materials/DAW/Tema 2.1 - Introducción a las Bases de datos.pdf");
        Blob material2Blob_DAW = loadResource("/static/assets/materials/DAW/Tema 2.2 - Bases de datos SQL en Spring.pdf");
        Blob material3Blob_DAW = loadResource("/static/assets/materials/DAW/Tema 2.3 - Generación de Tablas.pdf");
        Blob material4Blob_DAW = loadResource("/static/assets/materials/DAW/Tema 2.4 - Consultas.pdf");
        Blob material5Blob_DAW = loadResource("/static/assets/materials/DAW/Tema 2.5 - Gestión del esquema y Arquitectura.pdf");
        Blob material6Blob_DAW = loadResource("/static/assets/materials/DAW/Tema 2.6 - Ficheros e imágenes.pdf");
        Blob material7Blob_DAW = loadResource("/static/assets/materials/DAW/Tema 2.7 - Concurrencia.pdf");
    
        Material mat1_DAW = new Material("Tema 2.1 - Introducción a las Bases de datos.pdf", "application/pdf", material1Blob_DAW, course_DAW);
        Material mat2_DAW = new Material("Tema 2.2 - Bases de datos SQL en Spring.pdf", "application/pdf", material2Blob_DAW, course_DAW);
        Material mat3_DAW = new Material("Tema 2.3 - Generación de Tablas.pdf", "application/pdf", material3Blob_DAW, course_DAW);
        Material mat4_DAW = new Material("Tema 2.4 - Consultas.pdf", "application/pdf", material4Blob_DAW, course_DAW);
        Material mat5_DAW = new Material("Tema 2.5 - Gestión del esquema y Arquitectura.pdf", "application/pdf", material5Blob_DAW, course_DAW);
        Material mat6_DAW = new Material("Tema 2.6 - Ficheros e imágenes.pdf", "application/pdf", material6Blob_DAW, course_DAW);
        Material mat7_DAW = new Material("Tema 2.7 - Concurrencia.pdf", "application/pdf", material7Blob_DAW, course_DAW);

        materialRepository.saveAll(Arrays.asList(mat1_DAW, mat2_DAW, mat3_DAW, mat4_DAW, mat5_DAW, mat6_DAW, mat7_DAW));

        Blob material1Blob_EAS = loadResource("/static/assets/materials/EAS/T1 - Evolucion - v5.pdf");
        Blob material2Blob_EAS = loadResource("/static/assets/materials/EAS/T1.7 - Tipos Mantenimiento-v3 (entre diapositivas 27 y28 de T1).pdf");
        Blob material3Blob_EAS = loadResource("/static/assets/materials/EAS/T2 - Leyes de Lehman - v3.pdf");
        Blob material4Blob_EAS = loadResource("/static/assets/materials/EAS/T3 - Gestión de la Configuracion - v5.pdf");
        Blob material5Blob_EAS = loadResource("/static/assets/materials/EAS/T4 - Control de Versiones.pdf");
        Blob material6Blob_EAS = loadResource("/static/assets/materials/EAS/T5A - Principios SOLID - v6.pdf");
        Blob material7Blob_EAS = loadResource("/static/assets/materials/EAS/T5B - Principios RCC-ASS - v1.pdf");
        Blob material8Blob_EAS = loadResource("/static/assets/materials/EAS/T5C - Inversion.pdf");
    
        Material mat1_EAS = new Material("T1 - Evolucion - v5.pdf", "application/pdf", material1Blob_EAS, course_EAS);
        Material mat2_EAS = new Material("T1.7 - Tipos Mantenimiento-v3 (entre diapositivas 27 y28 de T1).pdf", "application/pdf", material2Blob_EAS, course_EAS);
        Material mat3_EAS = new Material("T2 - Leyes de Lehman - v3.pdf", "application/pdf", material3Blob_EAS, course_EAS);
        Material mat4_EAS = new Material("T3 - Gestión de la Configuracion - v5.pdf", "application/pdf", material4Blob_EAS, course_EAS);
        Material mat5_EAS = new Material("T4 - Control de Versiones.pdf", "application/pdf", material5Blob_EAS, course_EAS);
        Material mat6_EAS = new Material("T5A - Principios SOLID - v6.pdf", "application/pdf", material6Blob_EAS, course_EAS);
        Material mat7_EAS = new Material("T5B - Principios RCC-ASS - v1.pdf", "application/pdf", material7Blob_EAS, course_EAS);
        Material mat8_EAS = new Material("T5C - Inversion.pdf", "application/pdf", material8Blob_EAS, course_EAS);

        materialRepository.saveAll(Arrays.asList(mat1_EAS, mat2_EAS, mat3_EAS, mat4_EAS, mat5_EAS, mat6_EAS, mat7_EAS, mat8_EAS));

        Blob material1Blob_ED = loadResource("/static/assets/materials/ED/Tema0_Punteros_New.pdf");
        Blob material2Blob_ED = loadResource("/static/assets/materials/ED/Tema1_IntroduccionTAD_new.pdf");
        Blob material3Blob_ED = loadResource("/static/assets/materials/ED/Tema2_Listas_new.pdf");
        Blob material4Blob_ED = loadResource("/static/assets/materials/ED/Tema2.2_Pilas.pdf");
        Blob material5Blob_ED = loadResource("/static/assets/materials/ED/Tema2.3_Colas.pdf");
        Blob material6Blob_ED = loadResource("/static/assets/materials/ED/Tema3.1_Conjuntos.pdf");
        Blob material7Blob_ED = loadResource("/static/assets/materials/ED/Tema3.2_Arboles_Eq.pdf");
        Blob material8Blob_ED = loadResource("/static/assets/materials/ED/Tema3.3_Grafos_new.pdf");
    
        Material mat1_ED = new Material("Tema0_Punteros_New.pdf", "application/pdf", material1Blob_ED, course_ED);
        Material mat2_ED = new Material("Tema1_IntroduccionTAD_new.pdf", "application/pdf", material2Blob_ED, course_ED);
        Material mat3_ED = new Material("Tema2_Listas_new.pdf", "application/pdf", material3Blob_ED, course_ED);
        Material mat4_ED = new Material("Tema2.2_Pilas.pdf", "application/pdf", material4Blob_ED, course_ED);
        Material mat5_ED = new Material("Tema2.3_Colas.pdf", "application/pdf", material5Blob_ED, course_ED);
        Material mat6_ED = new Material("Tema3.1_Conjuntos.pdf", "application/pdf", material6Blob_ED, course_ED);
        Material mat7_ED = new Material("Tema3.2_Arboles_Eq.pdf", "application/pdf", material7Blob_ED, course_ED);
        Material mat8_ED = new Material("Tema3.3_Grafos_new.pdf", "application/pdf", material8Blob_ED, course_ED);

        materialRepository.saveAll(Arrays.asList(mat1_ED, mat2_ED, mat3_ED, mat4_ED, mat5_ED, mat6_ED, mat7_ED, mat8_ED));

        Blob material1Blob_IP = loadResource("/static/assets/materials/IP/Tema01_ElementosBasicos.pdf");
        Blob material2Blob_IP = loadResource("/static/assets/materials/IP/Tema02_InstruccionesEstructuradas.pdf");
        Blob material3Blob_IP = loadResource("/static/assets/materials/IP/Tema03_Subprogramacion.pdf");
        Blob material4Blob_IP = loadResource("/static/assets/materials/IP/Tema04_Recursividad.pdf");
        Blob material5Blob_IP = loadResource("/static/assets/materials/IP/Tema05_Arrays.pdf");
        Blob material6Blob_IP = loadResource("/static/assets/materials/IP/Tema06_RegistrosFicheros.pdf");
    
        Material mat1_IP = new Material("Tema01_ElementosBasicos.pdf", "application/pdf", material1Blob_IP, course_IP);
        Material mat2_IP = new Material("Tema02_InstruccionesEstructuradas.pdf", "application/pdf", material2Blob_IP, course_IP);
        Material mat3_IP = new Material("Tema03_Subprogramacion.pdf", "application/pdf", material3Blob_IP, course_IP);
        Material mat4_IP = new Material("Tema04_Recursividad.pdf", "application/pdf", material4Blob_IP, course_IP);
        Material mat5_IP = new Material("Tema05_Arrays.pdf", "application/pdf", material5Blob_IP, course_IP);
        Material mat6_IP = new Material("Tema06_RegistrosFicheros.pdf", "application/pdf", material6Blob_IP, course_IP);

        materialRepository.saveAll(Arrays.asList(mat1_IP, mat2_IP, mat3_IP, mat4_IP, mat5_IP, mat6_IP));

        Blob material1Blob_MDA = loadResource("/static/assets/materials/MDA/Tema 1 (Algebra).pdf");
        Blob material2Blob_MDA = loadResource("/static/assets/materials/MDA/Tema 1 (MD).pdf");
        Blob material3Blob_MDA = loadResource("/static/assets/materials/MDA/Tema 2 (Algebra).pdf");
        Blob material4Blob_MDA = loadResource("/static/assets/materials/MDA/Tema 2 (MD).pdf");
        Blob material5Blob_MDA = loadResource("/static/assets/materials/MDA/Tema 3 (Algebra).pdf");
        Blob material6Blob_MDA = loadResource("/static/assets/materials/MDA/Tema 3 (MD).pdf");
        Blob material7Blob_MDA = loadResource("/static/assets/materials/MDA/Tema 4 (Algebra).pdf");
        Blob material8Blob_MDA = loadResource("/static/assets/materials/MDA/Tema 4 (MD).pdf");
    
        Material mat1_MDA = new Material("Tema 1 (Algebra).pdf", "application/pdf", material1Blob_MDA, course_MDA);
        Material mat2_MDA = new Material("Tema 1 (MD).pdf", "application/pdf", material2Blob_MDA, course_MDA);
        Material mat3_MDA = new Material("Tema 2 (Algebra).pdf", "application/pdf", material3Blob_MDA, course_MDA);
        Material mat4_MDA = new Material("Tema 2 (MD).pdf", "application/pdf", material4Blob_MDA, course_MDA);
        Material mat5_MDA = new Material("Tema 3 (Algebra).pdf", "application/pdf", material5Blob_MDA, course_MDA);
        Material mat6_MDA = new Material("Tema 3 (MD).pdf", "application/pdf", material6Blob_MDA, course_MDA);
        Material mat7_MDA = new Material("Tema 4 (Algebra).pdf", "application/pdf", material7Blob_MDA, course_MDA);
        Material mat8_MDA = new Material("Tema 4 (MD).pdf", "application/pdf", material8Blob_MDA, course_MDA);

        materialRepository.saveAll(Arrays.asList(mat1_MDA, mat2_MDA, mat3_MDA, mat4_MDA, mat5_MDA, mat6_MDA, mat7_MDA, mat8_MDA));

        Blob material1Blob_POO = loadResource("/static/assets/materials/POO/Tema 1 - Introducción a la POO.pdf");
        Blob material2Blob_POO = loadResource("/static/assets/materials/POO/Tema 2 - Introducción al lenguaje Java.pdf");
        Blob material3Blob_POO = loadResource("/static/assets/materials/POO/Tema 3 - Herencia y polimorfismo.pdf");
        Blob material4Blob_POO = loadResource("/static/assets/materials/POO/Tema 4 - El paquete lang de Java.pdf");
        Blob material5Blob_POO = loadResource("/static/assets/materials/POO/Tema 5 - Diseño de clases.pdf");
        Blob material6Blob_POO = loadResource("/static/assets/materials/POO/Tema 6 - Genericidad y Estructuras de Datos.pdf");
        Blob material7Blob_POO = loadResource("/static/assets/materials/POO/Tema 7 - Entrada salida.pdf");
    
        Material mat1_POO = new Material("Tema 1 - Introducción a la POO.pdf", "application/pdf", material1Blob_POO, course_POO);
        Material mat2_POO = new Material("Tema 2 - Introducción al lenguaje Java.pdf", "application/pdf", material2Blob_POO, course_POO);
        Material mat3_POO = new Material("Tema 3 - Herencia y polimorfismo.pdf", "application/pdf", material3Blob_POO, course_POO);
        Material mat4_POO = new Material("Tema 4 - El paquete lang de Java.pdf", "application/pdf", material4Blob_POO, course_POO);
        Material mat5_POO = new Material("Tema 5 - Diseño de clases.pdf", "application/pdf", material5Blob_POO, course_POO);
        Material mat6_POO = new Material("Tema 6 - Genericidad y Estructuras de Datos.pdf", "application/pdf", material6Blob_POO, course_POO);
        Material mat7_POO = new Material("Tema 7 - Entrada salida.pdf", "application/pdf", material7Blob_POO, course_POO);

        materialRepository.saveAll(Arrays.asList(mat1_POO, mat2_POO, mat3_POO, mat4_POO, mat5_POO, mat6_POO, mat7_POO));

        Blob material1Blob_SI = loadResource("/static/assets/materials/SI/Tema 6 - Malware.pdf");
        Blob material2Blob_SI = loadResource("/static/assets/materials/SI/Tema 7 Criptografia.pdf");
        Blob material3Blob_SI = loadResource("/static/assets/materials/SI/Tema 8 - Contramedidas.pdf");
        Blob material4Blob_SI = loadResource("/static/assets/materials/SI/Tema 9 - ContramedidaUsuario.pdf");
        Blob material5Blob_SI = loadResource("/static/assets/materials/SI/Tema 10 - Presente y futuro.pdf");
        Blob material6Blob_SI = loadResource("/static/assets/materials/SI/Tema1 - Introduccion.pdf");
        Blob material7Blob_SI = loadResource("/static/assets/materials/SI/Tema2 - Conceptos.pdf");
        Blob material8Blob_SI = loadResource("/static/assets/materials/SI/Tema3 - AnatomiaAtaque.pdf");
        Blob material9Blob_SI = loadResource("/static/assets/materials/SI/Tema4 - AtaquesProtocolos.pdf");
        Blob material10Blob_SI = loadResource("/static/assets/materials/SI/Tema5 - AtaquesAplicaciones.pdf");
    
        Material mat1_SI = new Material("Tema 6 - Malware.pdf", "application/pdf", material1Blob_SI, course_SI);
        Material mat2_SI = new Material("Tema 7 Criptografia.pdf", "application/pdf", material2Blob_SI, course_SI);
        Material mat3_SI = new Material("Tema 8 - Contramedidas.pdf", "application/pdf", material3Blob_SI, course_SI);
        Material mat4_SI = new Material("Tema 9 - ContramedidaUsuario.pdf", "application/pdf", material4Blob_SI, course_SI);
        Material mat5_SI = new Material("Tema 10 - Presente y futuro.pdf", "application/pdf", material5Blob_SI, course_SI);
        Material mat6_SI = new Material("Tema1 - Introduccion.pdf", "application/pdf", material6Blob_SI, course_SI);
        Material mat7_SI = new Material("Tema2 - Conceptos.pdf", "application/pdf", material7Blob_SI, course_SI);
        Material mat8_SI = new Material("Tema3 - AnatomiaAtaque.pdf", "application/pdf", material8Blob_SI, course_SI);
        Material mat9_SI = new Material("Tema4 - AtaquesProtocolos.pdf", "application/pdf", material9Blob_SI, course_SI);
        Material mat10_SI = new Material("Tema5 - AtaquesAplicaciones.pdf", "application/pdf", material10Blob_SI, course_SI);

        materialRepository.saveAll(Arrays.asList(mat1_SI, mat2_SI, mat3_SI, mat4_SI, mat5_SI, mat6_SI, mat7_SI, mat8_SI, mat9_SI, mat10_SI));

        courseService.addUserToCourse(course_calculo, user1);
        courseService.addUserToCourse(course_calculo, user2);
        courseService.addUserToCourse(course_calculo, user3);
        courseService.addUserToCourse(course_calculo, user4);
        courseService.addUserToCourse(course_calculo, user5);
        courseService.addUserToCourse(course_calculo, user6);
        courseService.addUserToCourse(course_calculo, user7);

        courseService.addUserToCourse(course_DAA, user1);
        courseService.addUserToCourse(course_DAA, user3);
        courseService.addUserToCourse(course_DAA, user5);
        courseService.addUserToCourse(course_DAA, user7);
        courseService.addUserToCourse(course_DAA, user9);

        courseService.addUserToCourse(course_DAW, user2);
        courseService.addUserToCourse(course_DAW, user4);
        courseService.addUserToCourse(course_DAW, user6);
        courseService.addUserToCourse(course_DAW, user8);
        courseService.addUserToCourse(course_DAW, user10);

        courseService.addUserToCourse(course_EAS, user6);
        courseService.addUserToCourse(course_EAS, user7);
        courseService.addUserToCourse(course_EAS, user8);
        courseService.addUserToCourse(course_EAS, user9);
        courseService.addUserToCourse(course_EAS, user10);

        courseService.addUserToCourse(course_ED, user3);
        courseService.addUserToCourse(course_ED, user4);
        courseService.addUserToCourse(course_ED, user5);
        courseService.addUserToCourse(course_ED, user6);
        courseService.addUserToCourse(course_ED, user7);

        courseService.addUserToCourse(course_IP, user1);
        courseService.addUserToCourse(course_IP, user4);
        courseService.addUserToCourse(course_IP, user6);
        courseService.addUserToCourse(course_IP, user7);
        courseService.addUserToCourse(course_IP, user9);

        courseService.addUserToCourse(course_MDA, user1);
        courseService.addUserToCourse(course_MDA, user2);
        courseService.addUserToCourse(course_MDA, user6);
        courseService.addUserToCourse(course_MDA, user8);
        courseService.addUserToCourse(course_MDA, user9);
        courseService.addUserToCourse(course_MDA, user4);
        courseService.addUserToCourse(course_MDA, user5);
        courseService.addUserToCourse(course_MDA, user3);

        courseService.addUserToCourse(course_POO, user1);
        courseService.addUserToCourse(course_POO, user2);
        courseService.addUserToCourse(course_POO, user3);
        courseService.addUserToCourse(course_POO, user4);
        courseService.addUserToCourse(course_POO, user5);
        courseService.addUserToCourse(course_POO, user6);
        courseService.addUserToCourse(course_POO, user7);
        courseService.addUserToCourse(course_POO, user8);
        courseService.addUserToCourse(course_POO, user9);

        courseService.addUserToCourse(course_SI, user2);
        courseService.addUserToCourse(course_SI, user3);
        courseService.addUserToCourse(course_SI, user5);
        courseService.addUserToCourse(course_SI, user8);
        courseService.addUserToCourse(course_SI, user10);
    }
}
