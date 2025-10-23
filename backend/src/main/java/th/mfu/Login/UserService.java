package th.mfu.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import th.mfu.repository.UserRepository;
import th.mfu.model.User;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register: hashes password and saves. throws IllegalArgumentException on duplicate.
    public User register(User user) {
        if (user.getStudentId() != null && userRepository.existsByStudentId(user.getStudentId())) {
            throw new IllegalArgumentException("Student ID already used");
        }
        if (user.getEmail() != null && userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already used");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Authenticate: tries studentId first, then email. Returns Optional.empty() if not valid.
    public Optional<User> authenticate(String studentId, String email, String rawPassword) {
        if ((studentId == null || studentId.isBlank()) && (email == null || email.isBlank())) {
            return Optional.empty();
        }

        User user = null;
        if (studentId != null && !studentId.isBlank()) {
            user = userRepository.findByStudentId(studentId);
        }
        if (user == null && email != null && !email.isBlank()) {
            user = userRepository.findByEmail(email);
        }
        if (user == null) return Optional.empty();

        String stored = user.getPassword();
        if (stored == null) return Optional.empty();

        // BCrypt check if hash-looking, else plain text fallback
        if (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")) {
            if (passwordEncoder.matches(rawPassword, stored)) return Optional.of(user);
            return Optional.empty();
        } else {
            if (stored.equals(rawPassword)) return Optional.of(user);
            return Optional.empty();
        }
    }
}
