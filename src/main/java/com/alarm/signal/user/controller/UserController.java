package com.alarm.signal.user.controller;

import com.alarm.signal.user.dto.request.CreateUserRequest;
import com.alarm.signal.user.dto.request.UpdateRoleRequest;
import com.alarm.signal.user.dto.request.MakeProducerRequest;
import com.alarm.signal.user.dto.response.UserResponse;
import com.alarm.signal.user.dto.response.ExistsByEmailResponse;
import com.alarm.signal.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User management endpoints")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Create a new user")
    @PostMapping
    public UserResponse createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @Operation(summary = "Get user by email")
    @GetMapping("/by-email")
    public Optional<UserResponse> getByEmail(@RequestParam String email) {
        return userService.getByEmail(email);
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public Optional<UserResponse> getById(@PathVariable UUID id) {
        return userService.getById(id);
    }

    @Operation(summary = "Check if user exists by email")
    @GetMapping("/exists")
    public ExistsByEmailResponse existsByEmail(@RequestParam String email) {
        return userService.existsByEmail(email);
    }

    @Operation(summary = "Update user role")
    @PutMapping("/role")
    public UserResponse updateRole(@RequestBody UpdateRoleRequest request) {
        return userService.updateRole(request);
    }

    @Operation(summary = "Make user a producer")
    @PostMapping("/make-producer")
    public void makeProducer(@RequestBody MakeProducerRequest request) {
        userService.makeProducer(request);
    }
}
