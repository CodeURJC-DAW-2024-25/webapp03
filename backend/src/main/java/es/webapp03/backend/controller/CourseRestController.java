package es.webapp03.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import es.webapp03.backend.dto.CourseBasicDTO;
import es.webapp03.backend.dto.CourseDTO;
import es.webapp03.backend.service.CourseService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseRestController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable long id) {
        CourseDTO courseDTO = courseService.findById(id);
        return courseDTO != null ? ResponseEntity.ok(courseDTO) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCourse(@PathVariable long id) {
        if (courseService.existsById(id)) {
            courseService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<CourseBasicDTO>> filterCoursesByTags(@RequestParam List<String> tags) {
        List<CourseBasicDTO> courseDTOs = courseService.findByTags(tags);
        return ResponseEntity.ok(courseDTOs);
    }

    @PostMapping("/new")
    public ResponseEntity<CourseDTO> createCourse(@RequestParam String title,
            @RequestParam String description,
            @RequestParam(required = false) MultipartFile imageField,
            @RequestParam String tags) throws IOException {
        CourseBasicDTO courseDTO = new CourseBasicDTO(null, title, description, false, null, 0,
                List.of(tags.split(",")));
        CourseDTO savedCourse = courseService.save(courseDTO);
        return ResponseEntity.ok(savedCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> editCourse(@PathVariable Long id,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam(required = false) MultipartFile imageField,
            @RequestParam(required = false) boolean removeImage) throws IOException, SQLException {
        CourseDTO existingCourse = courseService.findById(id);
        if (existingCourse != null) {
            CourseBasicDTO updatedCourse = new CourseBasicDTO(id, title, description, existingCourse.image(),
                    existingCourse.imageFile(), existingCourse.numberOfUsers(), existingCourse.tags());
            CourseDTO savedCourse = courseService.save(updatedCourse);
            return ResponseEntity.ok(savedCourse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/loadMore")
    public ResponseEntity<Page<CourseBasicDTO>> loadMoreCourses(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<CourseBasicDTO> coursePage = courseService.findAll(pageable);
        return ResponseEntity.ok(coursePage);
    }
}