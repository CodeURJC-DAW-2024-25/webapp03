package es.webapp03.backend.controller;

import es.webapp03.backend.services.HtmlToPdfConverterService;
import es.webapp03.backend.model.User;
import es.webapp03.backend.model.Course;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private final HtmlToPdfConverterService htmlToPdfConverterService;

    public PdfController(HtmlToPdfConverterService htmlToPdfConverterService) {
        this.htmlToPdfConverterService = htmlToPdfConverterService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generatePdf(@RequestParam String templateName, @RequestParam String outputFileName) {
        try {
            // Prepare the data
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
            String templatePath = "src/main/resources/templates/" + templateName + ".html";
            String pdfFilePath = "output/" + outputFileName + ".pdf";
            htmlToPdfConverterService.generatePdfFromTemplate(templatePath, data, pdfFilePath);

            return ResponseEntity.ok("PDF generated successfully: " + pdfFilePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating PDF: " + e.getMessage());
        }
    }
}