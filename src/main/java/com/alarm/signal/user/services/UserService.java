package com.alarm.signal.user.services;

import com.alarm.signal.user.dto.request.CreateUserRequest;
import com.alarm.signal.user.dto.request.UpdateRoleRequest;
import com.alarm.signal.user.dto.request.MakeProducerRequest;
import com.alarm.signal.user.dto.response.UserResponse;
import com.alarm.signal.user.dto.response.ExistsByEmailResponse;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);
    Optional<UserResponse> getByEmail(String email);
    Optional<UserResponse> getById(UUID id);
    ExistsByEmailResponse existsByEmail(String email);
    UserResponse updateRole(UpdateRoleRequest request);
    void makeProducer(MakeProducerRequest request);
}
