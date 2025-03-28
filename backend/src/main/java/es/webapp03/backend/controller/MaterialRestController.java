package es.webapp03.backend.controller;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import es.webapp03.backend.dto.CourseDTO;
import es.webapp03.backend.dto.CourseMapper;
import es.webapp03.backend.dto.MaterialBasicDTO;
import es.webapp03.backend.dto.MaterialMapper;
import es.webapp03.backend.model.Material;
import es.webapp03.backend.model.Course;
import es.webapp03.backend.service.MaterialService;
import es.webapp03.backend.service.CourseService;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/materials")
public class MaterialRestController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private MaterialMapper materialMapper;

    // Endpoint upload material to course
    @PostMapping("/courses/{id}/")
    public ResponseEntity<MaterialBasicDTO> uploadFile(@PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            CourseDTO courseDTO = courseService.findById(id);
            if (courseDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Course course = courseMapper.toDomain(courseDTO);

            InputStream fileInputStream = file.getInputStream();
            Blob fileBlob = new SerialBlob(fileInputStream.readAllBytes());

            Material material = new Material(file.getOriginalFilename(), file.getContentType(), fileBlob, course);

            materialService.save(material);

            MaterialBasicDTO materialBasicDTO = materialMapper.toDTO(material);

            return ResponseEntity.status(HttpStatus.CREATED).body(materialBasicDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint download file from course
    @GetMapping("/courses/{courseId}/materials/{materialId}/")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long courseId, @PathVariable Long materialId)
            throws SQLException {
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

    // Endpoint delete file
    @DeleteMapping("/courses/{courseId}/materials/{materialId}/")
    public ResponseEntity<Void> deleteFile(@PathVariable Long courseId, @PathVariable Long materialId) {
        materialService.deleteById(materialId);
        return ResponseEntity.noContent().build();
    }

    // Endpoint load more files pageable
    @GetMapping("/courses/{courseId}/load")
    public ResponseEntity<Page<MaterialBasicDTO>> loadMoreMaterials(@PathVariable Long courseId, Pageable pageable) {
        Page<Material> materialsPage = materialService.findByCourseId(courseId, pageable);
        Page<MaterialBasicDTO> materialsDTO = materialsPage.map(material -> new MaterialBasicDTO(
                material.getId(),
                material.getName(),
                material.getType(),
                material.getUrl(),
                material.getFile()));

        return ResponseEntity.ok(materialsDTO);
    }
}
