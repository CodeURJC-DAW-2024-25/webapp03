package es.webapp03.backend.service;

import es.webapp03.backend.dto.CourseBasicDTO;
import es.webapp03.backend.dto.CourseDTO;
import es.webapp03.backend.dto.CourseMapper;
import es.webapp03.backend.model.Course;
import es.webapp03.backend.model.User;
import es.webapp03.backend.repository.CourseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseMapper;

    public CourseService() {
    }

    @Transactional
    public CourseDTO addUserToCourse(Long courseId, User user) {
        Optional<Course> optCourse = courseRepository.findById(courseId);
        if (optCourse.isPresent()) {
            Course course = optCourse.get();
            if (!course.getUsers().contains(user)) {
                course.addUser(user);
                int numberOfUsers = course.getNumberOfUsers();
                course.setNumberOfUsers(numberOfUsers + 1);
                courseRepository.save(course);
            }
            return courseMapper.toFullDTO(course);
        }
        return null;
    }

    public boolean isUserInCourse(Long courseId, String userEmail) {
        return courseRepository.findById(courseId)
                .map(course -> course.getUsers().stream().anyMatch(user -> user.getEmail().equals(userEmail)))
                .orElse(false);
    }

    public List<CourseBasicDTO> getTopCourses() {
        return courseMapper.toDTOs(courseRepository.findTop3ByOrderByNumberOfUsersDesc());
    }

    public CourseDTO findById(Long courseId) {
        return courseRepository.findById(courseId)
                .map(courseMapper::toFullDTO)
                .orElse(null);
    }

    public boolean existsById(long id) {
        return courseRepository.existsById(id);
    }

    public void deleteById(long id) {
        Optional<Course> optCourse = courseRepository.findById(id);
        optCourse.ifPresent(course -> {
            course.getUsers().forEach(user -> user.getCourses().remove(course));
            course.getUsers().clear();
            courseRepository.save(course);
            courseRepository.deleteById(id);
        });
    }

    public List<CourseBasicDTO> findByTags(List<String> tags) {
        return courseMapper.toDTOs(courseRepository.findByTags(tags));
    }

    public CourseDTO save(CourseBasicDTO courseDTO) {
        Course course = courseMapper.toDomain(courseDTO);
        return courseMapper.toFullDTO(courseRepository.save(course));
    }    

    public Page<CourseBasicDTO> findAll(Pageable pageable) {
        return courseRepository.findAll(pageable).map(courseMapper::toDTO);
    }
}
