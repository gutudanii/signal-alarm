package com.alarm.signal.auth;

import com.alarm.signal.auth.dto.LoginRequest;
import com.alarm.signal.security.JwtService;
import com.alarm.signal.user.model.User;
import com.alarm.signal.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email and password required"));
        }
        User user = userRepository.findByEmailIgnoreCase(email.trim().toLowerCase())
                .orElse(null);
        if (user == null || user.getPassword() == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
        String token = jwtService.generateToken(user.getEmail(), user.getRole() != null ? user.getRole().name() : "USER");
        return ResponseEntity.ok(Map.of("token", token));
    }
}
