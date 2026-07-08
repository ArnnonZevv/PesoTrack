package edu.cit.pangan.hellosummer.feature.auth.register;

public record RegisterRequest(
    String fullname,
    String email,
    String username,
    String password
) {}