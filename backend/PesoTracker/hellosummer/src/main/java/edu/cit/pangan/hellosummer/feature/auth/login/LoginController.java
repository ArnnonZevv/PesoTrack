package edu.cit.pangan.hellosummer.feature.auth.login;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.username() == null || request.username().isBlank()
                || request.password() == null || request.password().isBlank()) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Username and password are required."));
        }

        LoginResponse response = loginService.login(request);

        if (response == null) {
            return ResponseEntity.status(401)
                .body(Map.of("message", "Invalid username or password."));
        }

        return ResponseEntity.ok(response);
    }
}