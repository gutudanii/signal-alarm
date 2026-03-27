package com.alarm.signal.subscription.dto;

import com.alarm.signal.subscription.model.PriorityFilter;
import lombok.Data;
import java.util.UUID;

@Data
public class SubscriptionCreateRequest {
    private UUID userId;
    private UUID producerId;
    private PriorityFilter priorityFilter;
}

