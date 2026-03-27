package com.alarm.signal.user.dto.response;

import com.alarm.signal.user.model.enums.AuthProvider;
import com.alarm.signal.user.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private AuthProvider provider;
    private Set<Role> roles;
    private Instant createdAt;
}
