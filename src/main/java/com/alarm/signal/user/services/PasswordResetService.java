package com.alarm.signal.user.services;

import com.alarm.signal.user.dto.request.PasswordResetRequest;
import com.alarm.signal.user.dto.request.PasswordResetConfirmRequest;

public interface PasswordResetService {
    /**
     * Generates a password reset token and returns the reset link (no email sent).
     * @param request the password reset request
     * @param baseUrl the base URL to use for the reset link
     * @return the reset link (with token)
     */
    String createPasswordResetToken(PasswordResetRequest request, String baseUrl);
    void resetPassword(PasswordResetConfirmRequest request);

    /**
     * Change a user's authentication provider (GOOGLE <-> LOCAL) with validation.
     */
    void changeAuthProvider(String email, com.alarm.signal.user.model.enums.AuthProvider newProvider, String newPassword);
}
