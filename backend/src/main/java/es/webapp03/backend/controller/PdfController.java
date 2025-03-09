package es.webapp03.backend.controller;

import com.lowagie.text.DocumentException;
import es.webapp03.backend.model.Course;
import es.webapp03.backend.model.User;
import es.webapp03.backend.repository.CourseRepository;
import es.webapp03.backend.service.PdfService;
import es.webapp03.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping("/api/pdf/generate/{courseId}")
    public ResponseEntity<byte[]> generatePdf(@RequestParam String templateName, @RequestParam String outputFileName, @RequestParam("_csrf") String csrfToken, Principal principal, @PathVariable Long courseId) {
        if (principal == null) {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }

        try {
            // Load the template content from classpath
            ClassPathResource resource = new ClassPathResource("templates/" + templateName + ".html");
            String templateContent = new String(Files.readAllBytes(resource.getFile().toPath()));

            // Get the current user
            User user = userService.findByEmail(principal.getName());
            if (user == null) {
                return ResponseEntity.status(404).body(null); // User not found
            }

            Optional<Course> courseOpt = courseRepository.findById(courseId);
            if (courseOpt.isEmpty()) {
                return ResponseEntity.status(404).body(null); // Course not found
            }
            Course course = courseOpt.get();

            // Convert image to base64
            String base64Image = null;
            if (course.getImageFile() != null) {
                Blob imageBlob = course.getImageFile();
                try (InputStream inputStream = imageBlob.getBinaryStream()) {
                    byte[] imageBytes = inputStream.readAllBytes();
                    base64Image = Base64.getEncoder().encodeToString(imageBytes);
                } catch (SQLException e) {
                    return ResponseEntity.status(500).body(null); // Error reading image
                }
            }

            // Prepare data for the template
            Map<String, Object> modelMap = new HashMap<>();
            modelMap.put("userFormalName", user.getName()); // Set userName as a string
            modelMap.put("course", course);
            // modelMap.put("image", base64Image != null);
            modelMap.put("courseImageUrl", base64Image != null ? "data:image/jpeg;base64," + base64Image : null);

            // Generate PDF
            byte[] pdfBytes = pdfService.generatePdfFromTemplate(templateContent, modelMap);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", outputFileName + ".pdf");

            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (IOException | DocumentException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}