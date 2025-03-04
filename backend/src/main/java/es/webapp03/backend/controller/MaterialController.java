package es.webapp03.backend.controller;

import java.net.URI;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.webapp03.backend.model.Course;
import es.webapp03.backend.model.Material;
import es.webapp03.backend.repository.CourseRepository;
import es.webapp03.backend.repository.MaterialRepository;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MaterialController {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();

        if (principal != null) {
            model.addAttribute("logged", true);
            model.addAttribute("userName", principal.getName());
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
        } else {
            model.addAttribute("logged", false);
        }
    }

    @PostMapping("/courses/{courseId}/materials/upload")
    public ResponseEntity<Void> uploadFile(@PathVariable Long courseId, @RequestParam("file") MultipartFile file) {
        try {
            Optional<Course> courseOpt = courseRepository.findById(courseId);
            if (courseOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Course course = courseOpt.get();

            // Convert file to byte[]
            byte[] fileBytes = file.getBytes();

            // Save file in the database
            Material material = new Material(file.getOriginalFilename(), file.getContentType(), fileBytes, course);
            materialRepository.save(material);

            // Redirect to the course page
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/courses/" + courseId));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/courses/{courseId}/materials/{materialId}/download")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long courseId, @PathVariable Long materialId) {
        Optional<Material> materialOpt = materialRepository.findById(materialId);
        if (materialOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Material material = materialOpt.get();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + material.getName() + "\"")
                .contentType(MediaType.parseMediaType(material.getType()))
                .body(material.getFile());
    }

    @PostMapping("/courses/{courseId}/materials/{materialId}/delete")
    public ResponseEntity<byte[]> deleteFile(@PathVariable Long courseId, @PathVariable Long materialId) {

        Optional<Material> materialOpt = materialRepository.findById(materialId);
        if (materialOpt.isPresent()) {
            materialRepository.delete(materialOpt.get());
        }

        // Redirect to the course page
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/courses/" + courseId));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
