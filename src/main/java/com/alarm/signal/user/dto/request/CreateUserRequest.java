package com.alarm.signal.user.dto.request;

import com.alarm.signal.user.model.enums.AuthProvider;
import com.alarm.signal.user.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String password; // nullable for Google
    private AuthProvider provider;
//    private Set<Role> role;
//    private boolean isEmailVerified;
}
