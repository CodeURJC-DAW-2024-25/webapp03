package es.webapp03.backend.controller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import es.webapp03.backend.model.Course;
import es.webapp03.backend.model.User;
import es.webapp03.backend.service.UserService;
import es.webapp03.backend.service.CourseService;

@Controller
public class WebController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;


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

    @GetMapping("/")
    public String showCourses(Model model, @RequestParam(defaultValue = "0") int page) {
        int pageSize = 3; // Number of courses per page
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Course> coursePage = courseService.findAll(pageable);

        model.addAttribute("courses", coursePage.getContent()); // Only the courses on the current page
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());

        return "index";
    }

    @GetMapping("/chartData")
    public ResponseEntity<?> getChartData() {
        List<Course> topCourses = courseService.getTopCourses();
        List<Map<String, Object>> chartData = new ArrayList<>();

        for (Course course : topCourses) {
            Map<String, Object> courseData = new HashMap<>();
            courseData.put("name", course.getTitle());
            courseData.put("inscriptions", course.getNumberOfUsers());
            chartData.add(courseData);
        }

        return ResponseEntity.ok(chartData);
    }

    @GetMapping("/courses/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {
        Optional<Course> course = courseService.findById(id);
        if (course.isPresent() && course.get().getImageFile() != null) {
            Resource file = new InputStreamResource(course.get().getImageFile().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(course.get().getImageFile().length()).body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/profile_page")
    public String showProfilePage(Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            User user = userService.findByEmail(email);

            if (user != null) {
                model.addAttribute("name", user.getName());
                model.addAttribute("email", user.getEmail());
                model.addAttribute("imageFile", "/profile/image");
            }
        }
        return "profile_page";
    }

    @GetMapping("/newcourse")
    public String showNewCourse() {
        return "newcourse";
    }
}
