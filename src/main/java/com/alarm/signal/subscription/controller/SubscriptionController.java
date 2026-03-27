package com.alarm.signal.subscription.controller;

import com.alarm.signal.subscription.dto.SubscriptionCreateRequest;
import com.alarm.signal.subscription.dto.SubscriptionDto;
import com.alarm.signal.subscription.dto.SubscriptionUpdateRequest;
import com.alarm.signal.subscription.services.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@Tag(name = "Subscriptions", description = "APIs for managing user subscriptions to producers")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all subscriptions for a user", description = "Returns all subscriptions where the given user is the subscriber.")
    public ResponseEntity<List<SubscriptionDto>> getSubscriptionsByUser(
            @Parameter(description = "User ID (subscriber)") @PathVariable UUID userId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByUser(userId));
    }

    @GetMapping("/producer/{producerId}")
    @Operation(summary = "Get all subscriptions for a producer", description = "Returns all subscriptions where the given producer is being followed.")
    public ResponseEntity<List<SubscriptionDto>> getSubscriptionsByProducer(
            @Parameter(description = "Producer ID") @PathVariable UUID producerId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByProducer(producerId));
    }

    @GetMapping("/active/{producerId}")
    @Operation(summary = "Get all active, unmuted subscribers for a producer", description = "Returns all active and unmuted subscriptions for a producer (for notifications).")
    public ResponseEntity<List<SubscriptionDto>> getActiveUnmutedSubscribers(
            @Parameter(description = "Producer ID") @PathVariable UUID producerId) {
        return ResponseEntity.ok(subscriptionService.getActiveUnmutedSubscribers(producerId));
    }

    @PostMapping("/")
    @Operation(summary = "Subscribe a user to a producer", description = "Creates a new subscription from a user to a producer.")
    public ResponseEntity<SubscriptionDto> subscribe(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Subscription create request")
            @RequestBody SubscriptionCreateRequest request) {
        return ResponseEntity.ok(subscriptionService.subscribe(request));
    }

    @DeleteMapping("/")
    @Operation(summary = "Unsubscribe a user from a producer", description = "Removes a subscription from a user to a producer.")
    public ResponseEntity<Void> unsubscribe(
            @Parameter(description = "User ID (subscriber)") @RequestParam UUID userId,
            @Parameter(description = "Producer ID") @RequestParam UUID producerId) {
        subscriptionService.unsubscribe(userId, producerId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/")
    @Operation(summary = "Update a subscription", description = "Updates an existing subscription (mute, priority, etc.)")
    public ResponseEntity<SubscriptionDto> updateSubscription(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Subscription update request")
            @RequestBody SubscriptionUpdateRequest updateRequest) {
        return ResponseEntity.ok(subscriptionService.updateSubscription(updateRequest));
    }

    @PatchMapping("/mute")
    @Operation(summary = "Mute or unmute a producer for a user", description = "Sets the muted status for a user's subscription to a producer.")
    public ResponseEntity<SubscriptionDto> muteProducer(
            @Parameter(description = "User ID (subscriber)") @RequestParam UUID userId,
            @Parameter(description = "Producer ID") @RequestParam UUID producerId,
            @Parameter(description = "Muted (true to mute, false to unmute)") @RequestParam boolean muted) {
        return ResponseEntity.ok(subscriptionService.muteProducer(userId, producerId, muted));
    }
}
