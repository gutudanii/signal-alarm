package com.alarm.signal.user.services.impl;

import com.alarm.signal.common.exception.ServiceException;
import com.alarm.signal.common.email.EmailService;
import com.alarm.signal.user.dto.request.PasswordResetConfirmRequest;
import com.alarm.signal.user.dto.request.PasswordResetRequest;
import com.alarm.signal.user.model.PasswordResetToken;
import com.alarm.signal.user.model.User;
import com.alarm.signal.user.repository.PasswordResetTokenRepository;
import com.alarm.signal.user.repository.UserRepository;
import com.alarm.signal.user.services.PasswordResetService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    @Transactional
    public String createPasswordResetToken(PasswordResetRequest request, String baseUrl) {
        if (request == null || request.getEmail() == null || request.getEmail().isBlank()) {
            throw new ServiceException("Invalid email");
        }
        User user = userRepository.findByEmailIgnoreCase(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new ServiceException("User not found"));
        if (user.getProvider() != null && !user.getProvider().name().equalsIgnoreCase("LOCAL")) {
            throw new ServiceException("Password reset is not available for Google accounts.");
        }
        // Remove old tokens
        tokenRepository.deleteByUser(user);
        // Generate token
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusHours(1);
        PasswordResetToken resetToken = new PasswordResetToken(token, user, expiry);
        tokenRepository.save(resetToken);
        // Return the reset link
        String resetLink = baseUrl + "/api/auth/reset-password?token=" + token;
        // Send email
        try {
            java.util.Map<String, Object> variables = new java.util.HashMap<>();
            variables.put("name", user.getFirstName() + " " + user.getLastName());
            variables.put("resetLink", resetLink);
            emailService.sendHtmlMessage(
                user.getEmail(),
                "Password Reset Request",
                "password-reset",
                variables,
                null, null, null
            );
        } catch (MessagingException e) {
            throw new ServiceException("Failed to send password reset email: " + e.getMessage());
        }
        return resetLink;
    }

    @Override
    @Transactional
    public void resetPassword(PasswordResetConfirmRequest request) {
        if (request == null || request.getToken() == null || request.getToken().isBlank() ||
                request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            throw new ServiceException("Invalid reset request");
        }
        PasswordResetToken resetToken = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new ServiceException("Invalid or expired token"));
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ServiceException("Token expired");
        }
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        tokenRepository.delete(resetToken);
    }

    @Override
    @Transactional
    public void changeAuthProvider(String email, com.alarm.signal.user.model.enums.AuthProvider newProvider, String newPassword) {
        if (email == null || email.isBlank() || newProvider == null) {
            throw new ServiceException("Invalid request");
        }
        User user = userRepository.findByEmailIgnoreCase(email.trim().toLowerCase())
                .orElseThrow(() -> new ServiceException("User not found"));
        if (user.getProvider() == newProvider) {
            throw new ServiceException("User is already using " + newProvider.name() + " authentication.");
        }
        if (newProvider.name().equalsIgnoreCase("GOOGLE")) {
            // Switching to Google: remove password
            user.setPassword(null);
            user.setProvider(com.alarm.signal.user.model.enums.AuthProvider.GOOGLE);
        } else if (newProvider.name().equalsIgnoreCase("LOCAL")) {
            // Switching to LOCAL: require new password
            if (newPassword == null || newPassword.isBlank()) {
                throw new ServiceException("New password required to switch to LOCAL authentication.");
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setProvider(com.alarm.signal.user.model.enums.AuthProvider.LOCAL);
        } else {
            throw new ServiceException("Unsupported provider type.");
        }
        userRepository.save(user);
    }
}
