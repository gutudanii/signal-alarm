package com.alarm.signal.user.controller;

import com.alarm.signal.user.dto.request.RegisterDeviceRequest;
import com.alarm.signal.user.dto.response.UserDeviceResponse;
import com.alarm.signal.user.services.UserDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
@Tag(name = "User Device", description = "User device management endpoints")
public class UserDeviceController {
    private final UserDeviceService userDeviceService;

    @Operation(summary = "Register a new device for a user")
    @PostMapping("/register")
    public UserDeviceResponse registerDevice(@RequestBody RegisterDeviceRequest request) {
        return userDeviceService.registerDevice(request);
    }

    @Operation(summary = "Get all devices for a user")
    @GetMapping("/by-user/{userId}")
    public List<UserDeviceResponse> getDevicesByUser(@PathVariable UUID userId) {
        return userDeviceService.getDevicesByUser(userId);
    }

    @Operation(summary = "Get device by FCM token")
    @GetMapping("/by-token")
    public Optional<UserDeviceResponse> getByToken(@RequestParam String fcmToken) {
        return userDeviceService.getByToken(fcmToken);
    }

    @Operation(summary = "Remove a device for a user")
    @DeleteMapping("/remove")
    public void removeDevice(@RequestParam UUID userId, @RequestParam String fcmToken) {
        userDeviceService.removeDevice(userId, fcmToken);
    }
}
