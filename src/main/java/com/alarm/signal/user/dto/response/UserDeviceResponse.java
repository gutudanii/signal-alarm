package com.alarm.signal.user.dto.response;

import com.alarm.signal.user.model.enums.DevicePlatform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDeviceResponse {
    private UUID id;
    private UUID userId;
    private String fcmToken;
    private DevicePlatform platform;
    private Instant createdAt;
}

