package com.alarm.signal.user.services.impl;

import com.alarm.signal.user.dto.request.CreateUserRequest;
import com.alarm.signal.user.dto.request.UpdateRoleRequest;
import com.alarm.signal.user.dto.request.MakeProducerRequest;
import com.alarm.signal.user.dto.response.UserResponse;
import com.alarm.signal.user.dto.response.ExistsByEmailResponse;
import com.alarm.signal.user.model.enums.Role;
import com.alarm.signal.user.services.UserService;
import com.alarm.signal.user.repository.UserRepository;
import com.alarm.signal.user.mapper.UserMapper;
import com.alarm.signal.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        // 1. Validate email format
        String email = request.getEmail() == null ? null : request.getEmail().trim().toLowerCase();
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        // 2. Normalize email
        // Already done above
        // 3. Validate password strength if provider is LOCAL
        if (request.getProvider() == null || request.getProvider().name().equals("LOCAL")) {
            String password = request.getPassword();
            if (password == null || password.length() < 8) {
                throw new IllegalArgumentException("Password must be at least 8 characters");
            }
            // Add more password strength checks as needed
        }
        // 4. Check existsByEmail (case-insensitive)
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalStateException("User already exists with this email");
        }
        // 5. Hash password (never store plain text)
        User user = userMapper.toEntity(request);
        if (request.getProvider() == null || request.getProvider().name().equals("LOCAL")) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        } else {
            user.setPassword(null);
        }
        user.setEmail(email);
        // 6. Handle race condition (catch DB unique constraint violation)
        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            // Could be DataIntegrityViolationException
            throw new IllegalStateException("User already exists (race condition)");
        }
        // 7. Return UserResponse (never expose password)
        return userMapper.toResponse(user);
    }

    @Override
    public Optional<UserResponse> getByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        String normalized = email.trim().toLowerCase();
        return userRepository.findByEmailIgnoreCase(normalized)
                .map(userMapper::toResponse);
    }

    @Override
    public Optional<UserResponse> getById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        return userRepository.findById(id)
                .map(userMapper::toResponse);
    }

    @Override
    public ExistsByEmailResponse existsByEmail(String email) {
        if (email == null) {
            return ExistsByEmailResponse.builder().exists(false).build();
        }
        String normalized = email.trim().toLowerCase();
        boolean exists = userRepository.existsByEmailIgnoreCase(normalized);
        return ExistsByEmailResponse.builder().exists(exists).build();
    }

    @Override
    public UserResponse updateRole(UpdateRoleRequest request) {
        if (request == null || request.getUserId() == null || request.getNewRole() == null) {
            throw new IllegalArgumentException("Invalid request");
        }
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        // Prevent invalid transitions (add your own logic)
        user.setRole(request.getNewRole());
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    public void makeProducer(MakeProducerRequest request) {
        // This would require ProducerRepository, not implemented here
        // 1. Ensure user exists
        if (request == null || request.getUserId() == null) {
            throw new IllegalArgumentException("Invalid request");
        }
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        // 2. Ensure user is not already a producer (should check ProducerRepository)
        // 3. Idempotent: safe to call multiple times
        // 4. Assign role = PRODUCER
        user.setRole(Role.PRODUCER);
        userRepository.save(user);
    }
}
