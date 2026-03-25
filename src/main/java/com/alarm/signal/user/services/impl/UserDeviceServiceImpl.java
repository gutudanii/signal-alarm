package com.alarm.signal.user.services.impl;

import com.alarm.signal.common.exception.ServiceException;
import com.alarm.signal.user.dto.request.RegisterDeviceRequest;
import com.alarm.signal.user.dto.response.UserDeviceResponse;
import com.alarm.signal.user.services.UserDeviceService;
import com.alarm.signal.user.repository.UserDeviceRepository;
import com.alarm.signal.user.repository.UserRepository;
import com.alarm.signal.user.mapper.UserDeviceMapper;
import com.alarm.signal.user.model.UserDevice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDeviceServiceImpl implements UserDeviceService {
    private final UserDeviceRepository userDeviceRepository;
    private final UserRepository userRepository;
    private final UserDeviceMapper userDeviceMapper;

    @Override
    public UserDeviceResponse registerDevice(RegisterDeviceRequest request) {
        if (request == null || request.getUserId() == null || request.getFcmToken() == null || request.getFcmToken().isBlank() || request.getPlatform() == null) {
            throw new ServiceException("Invalid device registration request");
        }
        // Ensure user exists
        if (!userRepository.existsById(request.getUserId())) {
            throw new ServiceException("User does not exist");
        }
        // Prevent duplicates (idempotent): update if exists, else create
        Optional<UserDevice> existing = userDeviceRepository.findByFcmToken(request.getFcmToken());
        UserDevice device;
        if (existing.isPresent()) {
            device = existing.get();
            device.setUserId(request.getUserId());
            device.setPlatform(request.getPlatform());
        } else {
            device = userDeviceMapper.toEntity(request);
        }
        device = userDeviceRepository.save(device);
        return userDeviceMapper.toResponse(device);
    }

    @Override
    public List<UserDeviceResponse> getDevicesByUser(UUID userId) {
        if (userId == null || !userRepository.existsById(userId)) {
            return List.of();
        }
        return userDeviceRepository.findAllByUserId(userId)
                .stream()
                .map(userDeviceMapper::toResponse)
                .toList();
    }

    @Override
    public Optional<UserDeviceResponse> getByToken(String fcmToken) {
        if (fcmToken == null || fcmToken.isBlank()) return Optional.empty();
        return userDeviceRepository.findByFcmToken(fcmToken)
                .map(userDeviceMapper::toResponse);
    }

    @Override
    public void removeDevice(UUID userId, String fcmToken) {
        if (fcmToken == null || fcmToken.isBlank()) return;
        Optional<UserDevice> deviceOpt = userDeviceRepository.findByFcmToken(fcmToken);
        deviceOpt.ifPresent(userDeviceRepository::delete);
    }
}
