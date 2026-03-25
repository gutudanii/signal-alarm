package com.alarm.signal.user.services.impl;

import com.alarm.signal.common.exception.ServiceException;
import com.alarm.signal.user.dto.request.CreateProducerRequest;
import com.alarm.signal.user.dto.request.CreateUserRequest;
import com.alarm.signal.user.dto.request.UpdateRoleRequest;
import com.alarm.signal.user.dto.request.MakeProducerRequest;
import com.alarm.signal.user.dto.response.UserResponse;
import com.alarm.signal.user.dto.response.ExistsByEmailResponse;
import com.alarm.signal.user.model.enums.Role;
import com.alarm.signal.user.repository.ProducerRepository;
import com.alarm.signal.user.services.ProducerService;
import com.alarm.signal.user.services.UserService;
import com.alarm.signal.user.repository.UserRepository;
import com.alarm.signal.user.mapper.UserMapper;
import com.alarm.signal.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ProducerRepository producerRepository;
    private final ProducerService producerService;

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        // 1. Validate email format
        String email = request.getEmail() == null ? null : request.getEmail().trim().toLowerCase();
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new ServiceException("Invalid email format");
        }
        // 2. Normalize email
        // Already done above
        // 3. Validate password strength if provider is LOCAL
        if (request.getProvider() == null || request.getProvider().name().equals("LOCAL")) {
            String password = request.getPassword();
            if (password == null || password.length() < 8) {
                throw new ServiceException("Password must be at least 8 characters");
            }
            // Add more password strength checks as needed
        }
        // 4. Check existsByEmail (case-insensitive)
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ServiceException("User already exists with this email");
        }
        // 5. Hash password (never store plain text)
        User user = userMapper.toEntity(request);
        if (request.getProvider() == null || request.getProvider().name().equals("LOCAL")) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        } else {
            user.setPassword(null);
            user.setEmailVerified(true); // Google users are always verified
        }
        user.setEmail(email);
        // Ensure createdAt is set
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(Instant.now());
        }
        // 6. Handle race condition (catch DB unique constraint violation)
        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            throw new ServiceException("User already exists (race condition)");
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
            throw new ServiceException("Invalid request");
        }
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ServiceException("User not found"));

        // Prevent invalid transitions (add your own logic)
        user.setRole(request.getNewRole());
        user = userRepository.save(user);

        if (request.getNewRole() == Role.PRODUCER) {
            makeProducer(MakeProducerRequest.builder()
                    .userId(user.getId())
                    .build());
        }
        else if (request.getNewRole() == Role.USER) {
            // Optionally handle demotion from producer (not implemented here)
            producerRepository.deleteById(user.getId());
        }

        return userMapper.toResponse(user);
    }

    @Override
    public void makeProducer(MakeProducerRequest request) {
        // This would require ProducerRepository, not implemented here
        // 1. Ensure user exists
        if (request == null || request.getUserId() == null) {
            throw new ServiceException("Invalid request");
        }
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ServiceException("User not found"));
        // 2. Ensure user is not already a producer (should check ProducerRepository)
        if (producerRepository.existsByUserId(user.getId())) {
            throw new ServiceException("Producer already exists");
        }
        // 3. Idempotent: safe to call multiple times
        producerService.createProducer(CreateProducerRequest.builder()
                .userId(user.getId())
                .name(request.getProducerName())
                .build());
        // 4. Assign role = PRODUCER
        user.setRole(Role.PRODUCER);
        userRepository.save(user);
    }
}
