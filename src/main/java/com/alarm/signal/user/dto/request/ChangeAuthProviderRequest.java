package com.alarm.signal.user.dto.request;

import com.alarm.signal.user.model.enums.AuthProvider;

public class ChangeAuthProviderRequest {
    private String email;
    private AuthProvider newProvider;
    private String newPassword; // Only required if switching to LOCAL

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public AuthProvider getNewProvider() { return newProvider; }
    public void setNewProvider(AuthProvider newProvider) { this.newProvider = newProvider; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}

