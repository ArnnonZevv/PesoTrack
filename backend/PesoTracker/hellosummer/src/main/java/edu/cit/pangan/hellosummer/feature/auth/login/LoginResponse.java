package edu.cit.pangan.hellosummer.feature.auth.login;

public record LoginResponse(
    String token,
    Long   userId,
    String username,
    String fullname,
    String role
) {}