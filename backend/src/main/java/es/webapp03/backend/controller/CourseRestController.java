package es.webapp03.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import es.webapp03.backend.dto.CourseBasicDTO;
import es.webapp03.backend.dto.CourseDTO;
import es.webapp03.backend.service.CourseService;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.core.io.Resource;

@RestController
@RequestMapping("/api/courses")
public class CourseRestController {

    @Autowired
    private CourseService courseService;

    // Endpoint get all courses
    @GetMapping("/")
    public Page<CourseBasicDTO> getCourses(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return courseService.findAll(pageable);
    }

    // Endpoint get a course
    @GetMapping("/{id}")
    public CourseBasicDTO getCourse(@PathVariable long id) {
        return courseService.findByIdBasic(id);
    }

    // Endppint create a course
    @PostMapping("/")
    public ResponseEntity<CourseBasicDTO> createCourse(@RequestBody CourseBasicDTO courseBasicDTO) {
        CourseBasicDTO courseDTO = courseService.saveBasic(courseBasicDTO);

        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(courseDTO.id()).toUri();

        return ResponseEntity.created(location).body(courseDTO);
    }
    // Endpoint delete a course

    @DeleteMapping("/{id}")
    public CourseBasicDTO deleteCourse(@PathVariable long id) {
        return courseService.deleteByIdBasic(id);
    }

    // Endpoint edit a course
    @PutMapping("/{id}")
    public CourseBasicDTO editCourse(@PathVariable long id, @RequestBody CourseBasicDTO updatedCourseDTO)
            throws SQLException {
        return courseService.editCourseBasic(id, updatedCourseDTO);
    }
    // Endpoint course image

    @PostMapping("/{id}/image")
    public ResponseEntity<Object> createCourseImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
            throws IOException {

        courseService.createCourseImage(id, imageFile.getInputStream(), imageFile.getSize());

        URI location = fromCurrentRequest().build().toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Object> getCourseImage(@PathVariable long id) throws SQLException, IOException {

        Resource postImage = courseService.getCourseImage(id);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(postImage);

    }

    @PutMapping("/{id}/image")
    public ResponseEntity<Object> replaceCourseImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
            throws IOException {

        courseService.replaceCourseImage(id, imageFile.getInputStream(), imageFile.getSize());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteCourseImage(@PathVariable long id) throws IOException {

        courseService.deleteCourseImage(id);

        return ResponseEntity.noContent().build();
    }

}