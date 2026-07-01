package edu.cit.pangan.hellosummer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Registration/login used to be served here as Thymeleaf pages, but the
 * project now uses a React frontend that talks to AuthController's REST
 * endpoints under /api/auth instead. This just gives us a quick way to
 * confirm the backend is up.
 */
@RestController
public class IndexController {

    @GetMapping("/")
    public String home() {
        return "PesoTracker backend is running.";
    }
}