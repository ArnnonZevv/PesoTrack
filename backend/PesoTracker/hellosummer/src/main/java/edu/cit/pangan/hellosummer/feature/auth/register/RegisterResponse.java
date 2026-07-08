package edu.cit.pangan.hellosummer.feature.auth.register;

public record RegisterResponse(
    String token,
    Long   userId,
    String username,
    String fullname,
    String role
) {}