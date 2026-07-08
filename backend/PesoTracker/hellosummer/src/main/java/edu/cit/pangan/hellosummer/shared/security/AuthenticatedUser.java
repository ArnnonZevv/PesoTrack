package edu.cit.pangan.hellosummer.shared.security;

/**
 * Stored in Spring Security's context after a JWT is verified.
 * Controllers receive it via @AuthenticationPrincipal — no extra
 * database lookup needed.
 */
public record AuthenticatedUser(Long userId, String username, String role) {}