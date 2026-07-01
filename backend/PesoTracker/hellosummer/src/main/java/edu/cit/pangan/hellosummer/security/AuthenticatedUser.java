package edu.cit.pangan.hellosummer.security;

/**
 * What we attach to Spring Security's Authentication object once a JWT
 * has been verified. Lets controllers grab the current user's id directly
 * via @AuthenticationPrincipal instead of doing a lookup by username.
 */
public record AuthenticatedUser(Long userId, String username, String role) {
}