package edu.cit.pangan.hellosummer.controller;

import edu.cit.pangan.hellosummer.dto.AuthResponse;
import edu.cit.pangan.hellosummer.dto.LoginRequest;
import edu.cit.pangan.hellosummer.dto.RegisterRequest;
import edu.cit.pangan.hellosummer.entities.User;
import edu.cit.pangan.hellosummer.security.JwtUtil;
import edu.cit.pangan.hellosummer.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST API used by the React frontend (and, later, any other client) for
 * registration and login. Returns a JWT that the client stores and sends
 * back as "Authorization: Bearer <token>" on subsequent requests.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request.username() == null || request.username().isBlank()
                || request.password() == null || request.password().isBlank()
                || request.email() == null || request.email().isBlank()) {
            return ResponseEntity.badRequest().body("Fullname, email, username, and password are required.");
        }

        if (userService.usernameTaken(request.username())) {
            return ResponseEntity.status(409).body("Username is already taken.");
        }

        User saved = userService.save(new User(
                request.fullname(),
                request.email(),
                request.username(),
                request.password(),
                "USER"
        ));

        String token = jwtUtil.generateToken(saved.getId(), saved.getUsername(), saved.getRole());

        return ResponseEntity.ok(new AuthResponse(
                token, saved.getId(), saved.getUsername(), saved.getFullname(), saved.getRole()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userService.login(request.username(), request.password());

        if (user == null) {
            return ResponseEntity.status(401).body("Invalid username or password.");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        return ResponseEntity.ok(new AuthResponse(
                token, user.getId(), user.getUsername(), user.getFullname(), user.getRole()
        ));
    }
}
