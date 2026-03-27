package com.alarm.signal.user.model;

import com.alarm.signal.user.model.enums.DevicePlatform;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_devices",
    indexes = {
        @Index(name = "idx_user_devices_user", columnList = "userId")
    },
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"fcmToken"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDevice {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false, unique = true)
    private String fcmToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DevicePlatform platform;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}
