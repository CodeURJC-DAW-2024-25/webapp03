package es.webapp03.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.webapp03.backend.model.User;
import es.webapp03.backend.model.Course;
import es.webapp03.backend.repository.CourseRepository;
import es.webapp03.backend.repository.UserRepository;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public CourseService() {
    }

    public void addUserToCourse(Course course, User user){
        course.addUser(user);
        int numberOfUsers = course.getNumberOfUsers();
        course.setNumberOfUsers(numberOfUsers + 1);
        courseRepository.save(course);
    }

    public boolean isUserInCourse(Long courseId, String userEmail) {
        Optional<Course> optCourse = courseRepository.findById(courseId);
        Course course = null;
        if (optCourse.isPresent()) {
            course = optCourse.get();
        }
        return course != null && course.getUsers().stream().anyMatch(user -> user.getEmail().equals(userEmail));
    }

    public List<Course> getTopCourses(){
        return courseRepository.findTop3ByOrderByNumberOfUsersDesc();
    }

    public Optional<Course> findById(Long courseId) {
        return courseRepository.findById(courseId);
    }

    public boolean existsById(long id) {
        return courseRepository.existsById(id);
    }

    public void deleteById(long id) {
        courseRepository.deleteById(id);
    }

    public List<Course> findByTags(List<String> tags) {
        return courseRepository.findByTags(tags);
    }

    public void save(Course course) {
        courseRepository.save(course);
    }

    public Page<Course> findAll(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }
}
