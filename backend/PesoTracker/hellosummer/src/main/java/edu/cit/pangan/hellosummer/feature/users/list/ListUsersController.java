package edu.cit.pangan.hellosummer.feature.users.list;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ListUsersController {

    private final ListUsersService listUsersService;

    public ListUsersController(ListUsersService listUsersService) {
        this.listUsersService = listUsersService;
    }

    // Requires a valid JWT (SecurityConfig protects all /api/** except /api/auth/**)
    @GetMapping
    public List<UserSummary> all() {
        return listUsersService.getAll();
    }
}