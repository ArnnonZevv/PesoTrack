package edu.cit.pangan.hellosummer.feature.users.list;

import edu.cit.pangan.hellosummer.shared.entity.User;

public record UserSummary(Long id, String username, String fullname, String role) {
    public static UserSummary from(User u) {
        return new UserSummary(u.getId(), u.getUsername(), u.getFullname(), u.getRole());
    }
}