package edu.cit.pangan.hellosummer.service;

import edu.cit.pangan.hellosummer.entities.User;
import edu.cit.pangan.hellosummer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean usernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User login(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}