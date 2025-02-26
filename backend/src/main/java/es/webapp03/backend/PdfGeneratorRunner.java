package es.webapp03.backend;

import es.webapp03.backend.model.User;
import es.webapp03.backend.model.Course;
import es.webapp03.backend.services.HtmlToPdfConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PdfGeneratorRunner implements CommandLineRunner {

    @Autowired
    private HtmlToPdfConverterService htmlToPdfConverterService;

    @Override
    public void run(String... args) throws Exception {
        Map<String, Object> data = new HashMap<>();
        
        // Create a sample user
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setRoles(List.of("USER", "ADMIN"));
        
        // Create a list of sample courses
        List<Course> courses = new ArrayList<>();
        Course course1 = new Course("Course 1", "Description for Course 1", null);
        Course course2 = new Course("Course 2", "Description for Course 2", null);
        courses.add(course1);
        courses.add(course2);
        
        user.setCourses(courses);
        
        data.put("user", user);

        // Generate the PDF file
        String templatePath = "src/main/resources/templates/diploma.html";
        String pdfFilePath = "output/diploma.pdf";
        htmlToPdfConverterService.generatePdfFromTemplate(templatePath, data, pdfFilePath);
    }
}