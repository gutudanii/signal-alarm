package com.alarm.signal.user.dto.request;

import com.alarm.signal.user.model.enums.DevicePlatform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDeviceRequest {
    private UUID userId;
    private String fcmToken;
    private DevicePlatform platform;
}

