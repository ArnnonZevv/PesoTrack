package edu.cit.pangan.hellosummer.shared.repository;

import edu.cit.pangan.hellosummer.shared.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}