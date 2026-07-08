package edu.cit.pangan.hellosummer.feature.auth.login;

import edu.cit.pangan.hellosummer.shared.entity.User;
import edu.cit.pangan.hellosummer.shared.repository.UserRepository;
import edu.cit.pangan.hellosummer.shared.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil         jwtUtil;

    public LoginService(UserRepository  userRepository,
                        PasswordEncoder passwordEncoder,
                        JwtUtil         jwtUtil) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil         = jwtUtil;
    }

    /**
     * Returns a LoginResponse if credentials are valid, null if not.
     * Returning null instead of throwing keeps the controller's 401 logic
     * explicit and readable.
     */
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
            .filter(u -> passwordEncoder.matches(request.password(), u.getPassword()))
            .orElse(null);

        if (user == null) return null;

        String token = jwtUtil.generateToken(
            user.getId(), user.getUsername(), user.getRole()
        );

        return new LoginResponse(
            token, user.getId(), user.getUsername(),
            user.getFullname(), user.getRole()
        );
    }
}