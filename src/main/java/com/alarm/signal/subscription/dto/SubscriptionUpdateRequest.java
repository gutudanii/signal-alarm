package com.alarm.signal.subscription.dto;

import com.alarm.signal.subscription.model.PriorityFilter;
import lombok.Data;
import java.util.UUID;

@Data
public class SubscriptionUpdateRequest {
    private UUID id;
    private Boolean muted;
    private PriorityFilter priorityFilter;
    private Boolean active;
}
