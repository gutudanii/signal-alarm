package com.alarm.signal.user.services;

import com.alarm.signal.user.dto.request.RegisterDeviceRequest;
import com.alarm.signal.user.dto.response.UserDeviceResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDeviceService {
    UserDeviceResponse registerDevice(RegisterDeviceRequest request);
    List<UserDeviceResponse> getDevicesByUser(UUID userId);
    Optional<UserDeviceResponse> getByToken(String fcmToken);
    void removeDevice(UUID userId, String fcmToken);
}
