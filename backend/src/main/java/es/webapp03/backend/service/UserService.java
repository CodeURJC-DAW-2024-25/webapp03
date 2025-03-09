package es.webapp03.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.webapp03.backend.model.User;
import es.webapp03.backend.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user, String roleName) {
        user.setEncodedPassword(passwordEncoder.encode(user.getEncodedPassword()));
        user.getRoles().add(roleName);
        return userRepository.save(user);
    }

    public User findByName(String name) {
        return userRepository.findByName(name).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

}