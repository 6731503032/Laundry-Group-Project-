package th.mfu.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.mfu.model.User;
import th.mfu.service.UserService;
import th.mfu.model.Role; // adjust import if Role is in different package or if Role is not an enum

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Simple DTOs
    public static class LoginRequest {
        public String studentId; // optional
        public String email;     // optional
        public String password;  // required
    }

    public static class RegisterRequest {
        public String studentId;
        public String name;
        public String email;
        public String password;
        public String role; // e.g. "STUDENT" or "MANAGER"
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        if (req == null || ( (req.studentId==null || req.studentId.isBlank()) && (req.email==null || req.email.isBlank()) )) {
            return ResponseEntity.badRequest().body(Map.of("status","error","message","studentId or email required"));
        }
        if (req.password == null || req.password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("status","error","message","password required"));
        }

        Optional<User> uOpt = userService.authenticate(req.studentId, req.email, req.password);
        return uOpt.map(user -> {
            // DON'T return password
            return ResponseEntity.ok(Map.of(
                "status","ok",
                "userId", user.getId(),
                "studentId", user.getStudentId(),
                "name", user.getName(),
                "role", user.getRole() != null ? user.getRole().name() : null,
                "redirect", "/booking.html"
            ));
        }).orElseGet(() -> ResponseEntity.status(401).body(Map.of("status","error","message","Invalid credentials")));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (req == null || req.name == null || req.name.isBlank() || req.email == null || req.email.isBlank() || req.password == null || req.password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("status","error","message","name, email and password required"));
        }

        try {
            User user = new User();
            user.setStudentId(req.studentId);
            user.setName(req.name);
            user.setEmail(req.email);
            user.setPassword(req.password);

            // attempt to set Role enum if available
            if (req.role != null && !req.role.isBlank()) {
                try {
                    user.setRole(Role.valueOf(req.role.toUpperCase()));
                } catch (Exception ex) {
                    // if Role isn't an enum or invalid value, ignore (or set null)
                }
            }

            User saved = userService.register(user);
            return ResponseEntity.ok(Map.of("status","ok","id", saved.getId()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("status","error","message", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("status","error","message","Server error"));
        }
    }
}
