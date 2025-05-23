package es.webapp03.backend.service;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import es.webapp03.backend.dto.UserBasicDTO;
import es.webapp03.backend.dto.UserDTO;
import es.webapp03.backend.dto.UserMapper;
import es.webapp03.backend.dto.UserNoImageDTO;
import es.webapp03.backend.model.Course;
import es.webapp03.backend.model.User;
import es.webapp03.backend.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserBasicDTO registerUser(String name, String email, String password, String roleName) {

        User user = new User(name, email, passwordEncoder.encode(password), null, roleName);

        User savedUser = userRepository.save(user);
        return userMapper.toBasicDTO(savedUser);
    }

    public void addCourseToUser(Long userId, Course course) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.addCourse(course);
            userRepository.save(user);
        }
    }

    public UserBasicDTO findByName(String name) {
        return userRepository.findByName(name).map(userMapper::toBasicDTO).orElse(null);
    }

    public UserBasicDTO findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toBasicDTO).orElse(null);
    }

    public UserDTO findUserDTOByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toDTO).orElse(null);
    }

    public UserBasicDTO findUserBasicDTOById(Long id) {
        return userRepository.findById(id).map(userMapper::toBasicDTO).orElse(null);
    }

    public UserDTO findUserDTOById(Long id) {
        return userRepository.findById(id).map(userMapper::toDTO).orElse(null);
    }

    public User findEntityByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public void save(User newUser) {
        userRepository.save(newUser);
    }

    public List<UserBasicDTO> findByRoles(String role) {
        return userRepository.findByRoles(role).stream()
                .map(userMapper::toBasicDTO)
                .collect(Collectors.toList());
    }

    public boolean existsById(long id) {
        return userRepository.existsById(id);
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    public Page<UserNoImageDTO> findAllWithNoImage(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toNoImageDTO);
    }

    public Page<UserBasicDTO> findAllBasicUserDTO(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toBasicDTO);
    }

    public Page<UserDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDTO);
    }

    public UserDTO findUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toDTO).orElse(null);
    }

    public UserNoImageDTO findUserProfileById(Long id) {
        return userRepository.findById(id).map(userMapper::toNoImageDTO).orElse(null);
    }

    public void updateUserProfile(Long id, String name, String email, String password) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setName(name);
            user.setEmail(email);
            if (password != null && !password.isEmpty()) {
                user.setEncodedPassword(passwordEncoder.encode(password));
            }
            userRepository.save(user);
        }
    }

    public void updateUserProfileImage(Long id, InputStream inputStream, long size) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setImage(BlobProxy.generateProxy(inputStream, size));
            userRepository.save(user);
        }
    }

}
