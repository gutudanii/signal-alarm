package com.alarm.signal.subscription.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "subscriptions",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "producer_id"})
    },
    indexes = {
        @Index(name = "idx_subscriptions_producer", columnList = "producer_id"),
        @Index(name = "idx_subscriptions_user", columnList = "user_id"),
         @Index(name = "idx_subscriptions_producer_active", columnList = "producer_id, active, muted")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "producer_id", nullable = false)
    private UUID producerId;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean muted = false;

    @Column(name = "priority_filter")
    private PriorityFilter priorityFilter; // e.g., HIGH_ONLY, ALL

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    public void setActive(Boolean active) {
        if (active != null) {
            this.active = active;
        }
    }
}
