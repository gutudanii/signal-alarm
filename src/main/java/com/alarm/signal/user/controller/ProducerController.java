package com.alarm.signal.user.controller;

import com.alarm.signal.common.exception.ServiceException;
import com.alarm.signal.user.dto.request.CreateProducerRequest;
import com.alarm.signal.user.dto.response.ProducerResponse;
import com.alarm.signal.user.model.User;
import com.alarm.signal.user.model.enums.Role;
import com.alarm.signal.user.services.ProducerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.alarm.signal.security.UserPrincipal;

@RestController
@RequestMapping("/producers")
@RequiredArgsConstructor
@Tag(name = "Producer", description = "Producer management endpoints")
public class ProducerController {
    private final ProducerService producerService;

    @Operation(summary = "Create a new producer")
    @PostMapping
    public ProducerResponse createProducer(@RequestBody CreateProducerRequest request) {
        return producerService.createProducer(request);
    }

    @Operation(summary = "Get producer by user ID")
    @GetMapping("/by-user/{userId}")
    public Optional<ProducerResponse> getByUserId(@PathVariable UUID userId) {
        return producerService.getByUserId(userId);
    }

    @Operation(summary = "Get producer by ID")
    @GetMapping("/{id}")
    public Optional<ProducerResponse> getById(@PathVariable UUID id) {
        return producerService.getById(id);
    }

    @Operation(summary = "Check if producer exists by user ID")
    @GetMapping("/exists/by-user/{userId}")
    public boolean existsByUserId(@PathVariable UUID userId) {
        return producerService.existsByUserId(userId);
    }

    @Operation(summary = "Create a new producer (onboarding)")
    @PostMapping("/create")
    public ProducerResponse createProducer(@AuthenticationPrincipal UserPrincipal principal) {
        // Only allow if not already a producer
        User user = principal.getUser();
        if (user == null) throw new RuntimeException("Authenticated user not found");
        // Transactional flow: check role and producer record
        if (user.getRoles() == null || user.getRoles().contains(Role.PRODUCER)) {
            throw new ServiceException("User is already a producer");
        }
        // Call service to handle transactional onboarding
        return producerService.createProducerForAuthenticatedUser(user);
    }
}
