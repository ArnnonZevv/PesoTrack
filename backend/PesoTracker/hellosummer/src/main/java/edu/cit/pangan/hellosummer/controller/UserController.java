package edu.cit.pangan.hellosummer.controller;

import edu.cit.pangan.hellosummer.entities.User;
import edu.cit.pangan.hellosummer.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Requires a valid JWT (see SecurityConfig — everything except /api/auth/**
    // needs authentication). Handy for a quick "yes, users are really being
    // saved" check, and for the admin-style screenshot in the report.
    @GetMapping
    public List<User> allUsers() {
        return userService.getAllUsers();
    }
}
