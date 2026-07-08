package edu.cit.pangan.hellosummer.feature.users.list;

import edu.cit.pangan.hellosummer.shared.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListUsersService {

    private final UserRepository userRepository;

    public ListUsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserSummary> getAll() {
        return userRepository.findAll()
            .stream()
            .map(UserSummary::from)
            .toList();
    }
}