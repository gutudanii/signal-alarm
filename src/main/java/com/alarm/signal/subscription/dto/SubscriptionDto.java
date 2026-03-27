package com.alarm.signal.subscription.dto;

import com.alarm.signal.subscription.model.PriorityFilter;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDto {
    private UUID id;
    private UUID userId;
    private UUID producerId;
    private boolean active;
    private boolean muted;
    private PriorityFilter priorityFilter;
    private Instant createdAt;
    private Instant updatedAt;
}
