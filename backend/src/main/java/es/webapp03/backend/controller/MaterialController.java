package es.webapp03.backend.controller;

import java.io.InputStream;
import java.net.URI;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
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

import es.webapp03.backend.dto.CourseDTO;
import es.webapp03.backend.dto.CourseMapper;
import es.webapp03.backend.model.Course;
import es.webapp03.backend.model.Material;
import es.webapp03.backend.service.CourseService;
import es.webapp03.backend.service.MaterialService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MaterialController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private CourseMapper courseMapper;

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
            CourseDTO courseDTO = courseService.findById(courseId);
            Optional<Course> courseOpt = Optional.ofNullable(courseDTO != null ? courseMapper.toDomain(courseDTO) : null);

            if (courseOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Course course = courseOpt.get();

            // Convert file to Blob
            InputStream fileInputStream = file.getInputStream();
            Blob fileBlob = new SerialBlob(fileInputStream.readAllBytes());

            // Save file in the database as a Blob
            Material material = new Material(file.getOriginalFilename(), file.getContentType(), fileBlob, course);
            materialService.save(material);

            // Redirect to the course page
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/courses/" + courseId));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/courses/{courseId}/materials/{materialId}/download")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long courseId, @PathVariable Long materialId) throws SQLException {
        Optional<Material> materialOpt = materialService.findById(materialId);
        if (materialOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Material material = materialOpt.get();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + material.getName() + "\"")
                .contentType(MediaType.parseMediaType(material.getType()))
                .body(material.getFile().getBytes(1, (int) material.getFile().length()));
    }

    @PostMapping("/courses/{courseId}/materials/{materialId}/delete")
    public ResponseEntity<byte[]> deleteFile(@PathVariable Long courseId, @PathVariable Long materialId) {
        materialService.deleteById(materialId);

        // Redirect to the course page
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/courses/" + courseId));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
    
    @GetMapping("/courses/{courseId}/materials/load")
    public String loadMoreMaterials(@PathVariable Long courseId, @RequestParam int page, Model model) {
    int pageSize = 3; // Número de materiales por página
    Pageable pageable = PageRequest.of(page, pageSize);
    Page<Material> materialsPage = materialService.findByCourseId(courseId, pageable);

    model.addAttribute("material", materialsPage.getContent());
    return "fragments/materialList"; // Devuelve un fragmento de HTML con los materiales
}
}
