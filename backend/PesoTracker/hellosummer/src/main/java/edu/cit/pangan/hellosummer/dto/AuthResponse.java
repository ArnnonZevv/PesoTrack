package edu.cit.pangan.hellosummer.dto;

public record AuthResponse(String token, Long userId, String username, String fullname, String role) {
}