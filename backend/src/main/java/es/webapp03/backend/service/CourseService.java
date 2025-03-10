package es.webapp03.backend.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.webapp03.backend.model.User;
import es.webapp03.backend.model.Course;
import es.webapp03.backend.repository.CourseRepository;

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
        Optional<Course> optCourse = courseRepository.findById(id);
    
        if (optCourse.isPresent()) {
            Course course = optCourse.get();
    
            // Remove the relationship with users
            for (User user : new ArrayList<>(course.getUsers())) {
                user.getCourses().remove(course);
            }
            course.getUsers().clear(); // Clear the list of users in the course
    
            // Save changes to avoid integrity constraints
            courseRepository.save(course);
    
            // Delete the course
            courseRepository.deleteById(id);
        }
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
