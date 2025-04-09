package es.webapp03.backend.service;

import es.webapp03.backend.dto.CourseBasicDTO;
import es.webapp03.backend.dto.CourseDTO;
import es.webapp03.backend.dto.CourseMapper;
import es.webapp03.backend.model.Course;
import es.webapp03.backend.model.User;
import es.webapp03.backend.repository.CourseRepository;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
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

    public CourseBasicDTO findByIdBasic(Long courseId) {
        return courseRepository.findById(courseId)
                .map(courseMapper::toDTO)
                .orElse(null);
    }

    public boolean existsById(long id) {
        return courseRepository.existsById(id);
    }

    public CourseDTO editCourse(long id, CourseDTO updatedCourseDTO) throws SQLException {
        Course oldCourse = courseRepository.findById(id).orElseThrow();
        Course updatedCourse = courseMapper.toDomain(updatedCourseDTO);
        updatedCourse.setId(id);

        if (oldCourse.getImage() && updatedCourse.getImage()) {
            updatedCourse.setImageFile(BlobProxy.generateProxy(oldCourse.getImageFile().getBinaryStream(),
                    oldCourse.getImageFile().length()));
        }

        courseRepository.save(updatedCourse);

        return courseMapper.toFullDTO(updatedCourse);

    }

    public CourseBasicDTO editCourseBasic(long id, CourseBasicDTO updatedCourseDTO) throws SQLException {
        Course oldCourse = courseRepository.findById(id).orElseThrow();
        Course updatedCourse = courseMapper.toDomain(updatedCourseDTO);
        updatedCourse.setId(id);

        if (oldCourse.getImage() && updatedCourse.getImage()) {
            updatedCourse.setImageFile(BlobProxy.generateProxy(oldCourse.getImageFile().getBinaryStream(),
                    oldCourse.getImageFile().length()));
        }

        courseRepository.save(updatedCourse);

        return courseMapper.toDTO(updatedCourse);
    }

    public CourseDTO deleteById(long id) {
        Optional<Course> optCourse = courseRepository.findById(id);

        return optCourse.map(course -> {
            CourseDTO courseDTO = courseMapper.toFullDTO(course);
            course.getUsers().forEach(user -> user.getCourses().remove(course));
            course.getUsers().clear();
            courseRepository.save(course);
            courseRepository.deleteById(id);
            return courseDTO;
        }).orElse(null);
    }

    public CourseBasicDTO deleteByIdBasic(long id) {
        Optional<Course> optCourse = courseRepository.findById(id);

        return optCourse.map(course -> {
            CourseBasicDTO courseDTO = courseMapper.toDTO(course);
            course.getUsers().forEach(user -> user.getCourses().remove(course));
            course.getUsers().clear();
            courseRepository.save(course);
            courseRepository.deleteById(id);
            return courseDTO;
        }).orElse(null);
    }

    public List<CourseBasicDTO> findByTags(List<String> tags) {
        return courseMapper.toDTOs(courseRepository.findByTags(tags));
    }

    public CourseDTO save(CourseBasicDTO courseDTO) {
        Course course = courseMapper.toDomain(courseDTO);
        return courseMapper.toFullDTO(courseRepository.save(course));
    }

    public CourseBasicDTO saveBasic(CourseBasicDTO courseDTO) {
        Course course = courseMapper.toDomain(courseDTO);
        return courseMapper.toDTO(courseRepository.save(course));
    }

    public Page<CourseBasicDTO> findAll(Pageable pageable) {
        return courseRepository.findAll(pageable).map(courseMapper::toDTO);
    }

    // Image handeling

    public Resource getCourseImage(long id) throws SQLException {

        Course course = courseRepository.findById(id).orElseThrow();

        if (course.getImageFile() != null) {
            return new InputStreamResource(course.getImageFile().getBinaryStream());
        } else {
            throw new NoSuchElementException();
        }
    }

    public void postCourseImage(long id, InputStream inputStream, long size) {
        Course course = courseRepository.findById(id).orElseThrow();

        course.setImage(true);
        course.setImageFile(BlobProxy.generateProxy(inputStream, size));

        courseRepository.save(course);
    }

    public void putCourseImage(long id, InputStream inputStream, long size) {
        Course course = courseRepository.findById(id).orElseThrow();

        if (!course.getImage()) {
            throw new NoSuchElementException();
        }

        course.setImageFile(BlobProxy.generateProxy(inputStream, size));
        courseRepository.save(course);
    }

    public void deleteCourseImage(long id) {
        Course course = courseRepository.findById(id).orElseThrow();

        if (!course.getImage()) {
            throw new NoSuchElementException();
        }

        course.setImageFile(null);
        course.setImage(false);

        courseRepository.save(course);
    }
}
