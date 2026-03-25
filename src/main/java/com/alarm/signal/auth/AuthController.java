package com.alarm.signal.auth;

import com.alarm.signal.auth.dto.LoginRequest;
import com.alarm.signal.common.exception.ServiceException;
import com.alarm.signal.security.JwtService;
import com.alarm.signal.user.model.User;
import com.alarm.signal.user.repository.UserRepository;
import com.alarm.signal.user.dto.request.PasswordResetRequest;
import com.alarm.signal.user.dto.request.PasswordResetConfirmRequest;
import com.alarm.signal.user.services.PasswordResetService;
import com.alarm.signal.user.dto.request.ChangeAuthProviderRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "Authentication and password management endpoints")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {
        String email = request.getEmail();
        String password = request.getPassword();
        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email and password required"));
        }
        User user = userRepository.findByEmailIgnoreCase(email.trim().toLowerCase())
                .orElse(null);
        assert user != null;
        if (user.getProvider().name().equalsIgnoreCase("GOOGLE")) {
            String googleAuthUrl = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort() + "/oauth2/authorization/google";
            return ResponseEntity.status(400).body(Map.of("error", "Use Google login: " + googleAuthUrl));
        }
        else if (user.getPassword() == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
        String token = jwtService.generateToken(user.getEmail(), user.getRole() != null ? user.getRole().name() : "USER");
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<?> requestPasswordReset(@RequestBody PasswordResetRequest request, @RequestHeader(value = "Origin", required = false) String origin) {
        String baseUrl = (origin != null && !origin.isBlank()) ? origin : "http://localhost:8080";
        try {
            String resetLink = passwordResetService.createPasswordResetToken(request, baseUrl);
            return ResponseEntity.ok(Map.of(
                "message", "If the email exists, a reset link has been generated.",
                "resetLink", resetLink
            ));
        } catch (ServiceException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetConfirmRequest request) {
        passwordResetService.resetPassword(request);
        return ResponseEntity.ok(Map.of("message", "Password has been reset successfully."));
    }

    @PostMapping("/change-provider")
    public ResponseEntity<?> changeAuthProvider(@RequestBody ChangeAuthProviderRequest request) {
        try {
            passwordResetService.changeAuthProvider(request.getEmail(), request.getNewProvider(), request.getNewPassword());
            return ResponseEntity.ok(Map.of("message", "Authentication provider changed successfully."));
        } catch (ServiceException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }
}
