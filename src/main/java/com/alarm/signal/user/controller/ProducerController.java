package com.alarm.signal.user.controller;

import com.alarm.signal.user.dto.request.CreateProducerRequest;
import com.alarm.signal.user.dto.response.ProducerResponse;
import com.alarm.signal.user.services.ProducerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

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
}
