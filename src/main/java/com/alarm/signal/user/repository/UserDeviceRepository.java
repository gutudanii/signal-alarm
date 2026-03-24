package com.alarm.signal.user.repository;

import com.alarm.signal.user.model.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDeviceRepository extends JpaRepository<UserDevice, UUID> {
    Optional<UserDevice> findByFcmToken(String fcmToken);
    boolean existsByFcmToken(String fcmToken);
    List<UserDevice> findAllByUserId(UUID userId);
}
