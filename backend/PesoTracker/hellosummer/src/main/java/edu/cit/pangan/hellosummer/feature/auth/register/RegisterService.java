package edu.cit.pangan.hellosummer.feature.auth.register;

import edu.cit.pangan.hellosummer.shared.entity.User;
import edu.cit.pangan.hellosummer.shared.repository.UserRepository;
import edu.cit.pangan.hellosummer.shared.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil         jwtUtil;

    public RegisterService(UserRepository  userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil         jwtUtil) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil         = jwtUtil;
    }

    public RegisterResponse register(RegisterRequest request) {
        // Validation: username must be unique
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new IllegalStateException("Username is already taken.");
        }

        // Create and persist the new user with a hashed password
        User user = new User(
            request.fullname(),
            request.email(),
            request.username(),
            passwordEncoder.encode(request.password()),
            "USER"
        );
        User saved = userRepository.save(user);

        // Issue a JWT so the user is logged in immediately after registering
        String token = jwtUtil.generateToken(
            saved.getId(), saved.getUsername(), saved.getRole()
        );

        return new RegisterResponse(
            token, saved.getId(), saved.getUsername(),
            saved.getFullname(), saved.getRole()
        );
    }
}