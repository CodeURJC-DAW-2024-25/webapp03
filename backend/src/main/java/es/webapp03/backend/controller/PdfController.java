package es.webapp03.backend.controller;

import com.lowagie.text.DocumentException;

import es.webapp03.backend.model.Course;
import es.webapp03.backend.model.User;
import es.webapp03.backend.repository.CourseRepository;
import es.webapp03.backend.service.PdfService;
import es.webapp03.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
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

            // Prepare data for the template
            Map<String, Object> modelMap = new HashMap<>();
            modelMap.put("userFormalName", user.getName()); // Set userName as a string
            modelMap.put("course", course);

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