package edu.cit.pangan.hellosummer.feature.auth.register;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // Basic input validation before touching the database
        if (isBlank(request.fullname()) || isBlank(request.email())
                || isBlank(request.username()) || isBlank(request.password())) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Fullname, email, username, and password are required."));
        }
        if (request.password().length() < 6) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Password must be at least 6 characters."));
        }

        try {
            RegisterResponse response = registerService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalStateException e) {
            // Username taken
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", e.getMessage()));
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}